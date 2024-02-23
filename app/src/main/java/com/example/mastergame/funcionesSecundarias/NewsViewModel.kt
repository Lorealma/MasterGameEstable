package com.example.mastergame.funcionesSecundarias

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mastergame.model.Article
import com.example.mastergame.model.NoticiasResponse
import com.example.mastergame.repository.NoticiasRepositorio
import com.example.mastergame.util.Recursos
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query
import java.io.IOException
import java.util.Locale.IsoCountryCode

class NewsViewModel(app: Application, val noticiasRepositorio: NoticiasRepositorio):AndroidViewModel(app) {

    //posibles variables mal

    val headlines: MutableLiveData<Recursos<NoticiasResponse>> = MutableLiveData()
    var headLinesPage = 1
    var headLinesResponse: NoticiasResponse? = null

    val searchNews: MutableLiveData<Recursos<NoticiasResponse>> = MutableLiveData()
    var searNewsPage = 1
    var searchNewResponse: NoticiasResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    init{
        getHeadLines("us")

    }

    fun getHeadLines(countryCode: String)= viewModelScope.launch {
        headlinesInternet(countryCode)
    }

    fun searchNews(searchQuery: String)= viewModelScope.launch {
        searchNewsInternet(searchQuery)
    }

    private fun handleHeadlinesResponse(response: Response<NoticiasResponse>): Recursos<NoticiasResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headLinesPage++
                if (headLinesResponse == null) {
                    headLinesResponse = resultResponse

                } else {
                    val oldArticle = headLinesResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticle?.addAll(newArticles)

                }
                return Recursos.Success(headLinesResponse ?: resultResponse)

            }


        }
        return Recursos.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NoticiasResponse>): Recursos<NoticiasResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewResponse == null || newSearchQuery != oldSearchQuery) {
                    searNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewResponse = resultResponse
                } else {
                    searNewsPage++
                    val oldArticle = searchNewResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)

                }
                return Recursos.Success(searchNewResponse?:resultResponse)

            }

        }
        return Recursos.Error(response.message())

    }

    fun addToFavourites(article: Article) = viewModelScope.launch {
        noticiasRepositorio.upsert(article)
    }
    fun getFavoriteNews() = noticiasRepositorio.getFavoritos()

    fun deleteArticle (article: Article) = viewModelScope.launch {
        noticiasRepositorio.deleteArticulo(article)
    }
    fun internetConnection(context: Context): Boolean{
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager). apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                    else->false

                }
            }?:false

        }

    }
    private suspend fun headlinesInternet(countryCode: String){
        headlines.postValue(Recursos.Cargando())
        try {
            if (internetConnection(this.getApplication())){
                val response = noticiasRepositorio.getTitulos(countryCode,headLinesPage)
                headlines.postValue(handleHeadlinesResponse(response))
            }else{
                headlines.postValue(Recursos.Error("No tienes interne"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException->headlines.postValue(Recursos.Error("Unable to connect"))
                else->headlines.postValue(Recursos.Error("No hay Señal Mugiwara"))
            }
        }
    }
    private suspend fun searchNewsInternet (searchQuery: String){
        newSearchQuery = searchQuery
        searchNews.postValue(Recursos.Cargando())
        try{
            if(internetConnection(this.getApplication())){
                val response = noticiasRepositorio.buscarNoticias(searchQuery,searNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(Recursos.Error("No se puede coenctar a internet"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> searchNews.postValue(Recursos.Error("unable to conect"))
                else-> searchNews.postValue(Recursos.Error("No hay señal Mugiwara"))
            }
        }

    }
}