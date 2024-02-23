package com.example.mastergame.api

import com.example.mastergame.model.NoticiasResponse
import com.example.mastergame.util.ConstanteNoticias.Companion.API_KEY
import com.google.android.gms.common.api.internal.ApiKey
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale.IsoCountryCode

//Interfaz para la API de noticias
interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getHeadLines(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("category")
        categoryCode: String = "technology",
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response <NoticiasResponse>

    @GET ("v2/everything")
    suspend fun searchForNews (
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ):Response <NoticiasResponse>

}