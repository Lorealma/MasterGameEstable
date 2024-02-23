package com.example.mastergame.model

import com.google.firebase.Timestamp

data class VideoModel(
    var videoId: String = "",
    var titulo: String= "",
    var url: String = "",
    var uploaderId: String = "",
    var categoria: String = "",



//es importante el timestamp de firebase
    var createdTime: Timestamp = Timestamp.now()


)
