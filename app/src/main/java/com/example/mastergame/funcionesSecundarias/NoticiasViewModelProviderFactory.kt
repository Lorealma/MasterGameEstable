package com.example.mastergame.funcionesSecundarias

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mastergame.repository.NoticiasRepositorio

class NoticiasViewModelProviderFactory(val app: Application, val noticiasRepositorio: NoticiasRepositorio): ViewModelProvider.Factory {
    override fun <T:ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, noticiasRepositorio) as T
    }
}