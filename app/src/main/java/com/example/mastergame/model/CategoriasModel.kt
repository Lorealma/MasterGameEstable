package com.example.mastergame.model

data class CategoriasModel(
    var name : String = "",
    var coverUrl: String = "",
    var videos : MutableList<String>
){
    constructor() : this("","", mutableListOf())
}
