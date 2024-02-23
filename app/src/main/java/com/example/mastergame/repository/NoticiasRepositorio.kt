package com.example.mastergame.repository

import androidx.room.Query
import com.example.mastergame.api.RetrofitInstance
import com.example.mastergame.db.ArticulosDatabase
import com.example.mastergame.model.Article
import java.util.Locale.IsoCountryCode

class NoticiasRepositorio(val db: ArticulosDatabase) {

    suspend fun getTitulos(countryCode: String,pageNumber: Int) =
        RetrofitInstance.api.getHeadLines(countryCode,pageNumber)

    suspend fun buscarNoticias(searchQuery: String,pageNumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article)= db.getArticulosDao().upsert(article)

    fun getFavoritos()= db.getArticulosDao().getAllArticles()

    suspend fun  deleteArticulo(article: Article)= db.getArticulosDao().deleteArticle(article)




}