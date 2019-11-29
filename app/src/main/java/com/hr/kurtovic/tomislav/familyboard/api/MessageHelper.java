package com.hr.kurtovic.tomislav.familyboard.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.hr.kurtovic.tomislav.familyboard.models.Message;
import com.hr.kurtovic.tomislav.familyboard.models.User;


public class MessageHelper {

    private static final String COLLECTION_NAME = "message";

    public static Query getAllMessageForChat(String chat) {
        return ChatHelper.getChatCollection()
                .document(chat)
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated")
                .limit(50);
    }

    public static Task<DocumentReference> createMessageForChat(String textMessage, String chat, User userSender) {
        //Create message object
        Message message = new Message(textMessage, userSender);

        return ChatHelper.getChatCollection()
                .document(chat)
                .collection(COLLECTION_NAME)
                .add(message);
    }

    public static Task<Void> setMessageAccepted(Message message, String chat) {

        return ChatHelper.getChatCollection()
                .document(chat)
                .collection(COLLECTION_NAME)
                .document(message.getID())
                .update("accepted", true);
    }

    public static Task<DocumentReference> createMessageWithImageForChat(String urlImage, String textMessage, String chat, User userSender) {
        Message message = new Message(textMessage, userSender, urlImage);

        return ChatHelper.getChatCollection()
                .document(chat)
                .collection(COLLECTION_NAME)
                .add(message);
    }

    public static Task<Void> setMessageId(Message message, String id, String chat) {
        return ChatHelper.getChatCollection()
                .document(chat)
                .collection(COLLECTION_NAME)
                .document(id)
                .update("id", id);
    }
}
