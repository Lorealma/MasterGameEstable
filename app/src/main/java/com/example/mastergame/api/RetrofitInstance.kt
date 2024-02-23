package com.example.mastergame.api
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import com.example.mastergame.util.ConstanteNoticias.Companion.Base_URL

class RetrofitInstance {
    companion object{

        private val retrofit by lazy{

            val loggin = HttpLoggingInterceptor()
            loggin.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggin)
                .build()

            Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            }
        val api by lazy {
            retrofit.create(NewsApi::class.java)

        }
    }


}