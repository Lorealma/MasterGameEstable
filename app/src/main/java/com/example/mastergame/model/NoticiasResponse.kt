package com.example.mastergame.model

data class NoticiasResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)