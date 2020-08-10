import * as functions from 'firebase-functions';
import { DocumentSnapshot } from 'firebase-functions/lib/providers/firestore';

const admin = require('firebase-admin')
admin.initializeApp();

const VERSION_PATH = 'Versions/{version}'
const FAMILIES_PATH = `${VERSION_PATH}/Families`
const MESSAGES_IN_FAMILY_PATH = `${FAMILIES_PATH}/{family}/Messages`


exports.notifyNewMessage = functions.firestore
    .document(`${MESSAGES_IN_FAMILY_PATH}/{message}`)
    .onCreate((documentSnapshot, context) => {
        const message = documentSnapshot.data()

        const version = context.params.version
        const familyName = context.params.family
        const membersPath = `Versions/${version}/Members`

        const senderId = message.memberSenderRef.id

        const collection = `Versions/${version}/Families/${familyName}/Members`

        console.log(collection)

        return admin.firestore().collection(collection)
            .get()
            .then((querySnapshot: any[]) => {
                retrieveFamilyMembersAndSendToDevice(senderId, familyName, querySnapshot)
            })
            .catch((error: string) => {
                console.error('Error while retrieving members from family: ' + error)
            })


        // tslint:disable-next-line: no-shadowed-variable
        function retrieveFamilyMembersAndSendToDevice(senderId: String, familyName: String, querySnapshot: any[]) {

            querySnapshot.forEach(async function (familyMemberSnapshot) {

                if (familyMemberSnapshot.id === senderId) {
                    return
                }

                const familyMemberPath = `${membersPath}/${familyMemberSnapshot.id}`

                const familyMember = (await retrieveFamilyMember(familyMemberPath)).data()

                if (!familyMember) {
                    return
                }

                const registrationTokens = familyMember.registrationTokens

                const notificationBody = `New message in ${familyName}`
                const payload = makePayload(notificationBody)

                console.log({ payload })
                return admin.messaging().sendToDevice(registrationTokens, payload).then(
                    (response: { results: any[]; }) => checkRegisteredTokensAndUpdate(response, registrationTokens, familyMemberPath)
                )
            })
        }

        function checkRegisteredTokensAndUpdate(response: { results: any; }, registrationTokens: { [x: string]: any; }, familyMemberPath: String) {

            const stillRegisteredTokens = registrationTokens
            response.results.forEach((result: { error: any; }, index: string | number) => {
                const error = result.error;
                if (error) {
                    const failedRegistrationToken = registrationTokens[index]
                    console.error('Failure sending notification to device', failedRegistrationToken, error)

                    if (error.code === 'messaging/invalid-registration-token' || error.code === 'messaging/registration-token-not-registered') {
                        const failedIndex = stillRegisteredTokens.indexOf(failedRegistrationToken)
                        if (failedIndex > -1) {
                            stillRegisteredTokens.splice(failedIndex, 1)
                        }
                    }

                }
            });

            return admin.firestore().doc(familyMemberPath).update({
                registrationTokens: stillRegisteredTokens
            })

        }

        async function retrieveFamilyMember(path: String): Promise<DocumentSnapshot> {
            return admin.firestore().doc(path)
                .get()
                .catch((error: any) => {
                    console.log(error)
                });
        }

        function makePayload(notificationBody: String) {
            return {
                notification: {
                    title: "A family member sent a new message!",
                    body: notificationBody
                }
            }
        }
    })

