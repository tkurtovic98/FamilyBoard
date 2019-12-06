package com.hr.kurtovic.tomislav.familyboard.models

class User {

    var uid: String? = null
    var name: String? = null
    var role: String? = null
    var urlPicture: String? = null

    constructor() {}

    constructor(uid: String, name: String, urlPicture: String?) {
        this.uid = uid
        this.name = name
        this.urlPicture = urlPicture
    }


}
