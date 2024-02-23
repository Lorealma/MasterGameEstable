package com.example.mastergame.model

import android.security.identity.AccessControlProfile



data class UserModel(
    var id : String = "",
    var email : String= "",
    var username: String = "",
    var profilePic: String= "",
    // listas mutables de followers
    var followerList: MutableList<String>,
    var followingList: MutableList<String>,
    var likes: MutableList<String>



){
    constructor() : this ("","","","", mutableListOf(), mutableListOf(), mutableListOf())
}
