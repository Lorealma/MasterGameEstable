package com.example.mastergame.funcionesSecundarias.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.mastergame.R
import com.example.mastergame.databinding.FragmentArticulosFragmentosBinding
import com.example.mastergame.funcionesSecundarias.NewsViewModel
import com.example.mastergame.funcionesSecundarias.Noticias
import com.google.android.material.snackbar.Snackbar
import io.grpc.NameResolver.Args


class ArticulosFragmentos : Fragment(R.layout.fragment_articulos__fragmentos) {

    lateinit var  newsViewModel: NewsViewModel
    val args: ArticulosFragmentosArgs by navArgs()
    lateinit var  binding: FragmentArticulosFragmentosBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticulosFragmentosBinding.bind(view)

        newsViewModel = (activity as Noticias).newsViewModel
        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
          article.url.let{
            loadUrl(it)
          }
        }
        binding.fab.setOnClickListener{
            newsViewModel.addToFavourites(article)
            Snackbar.make(view,"añadidos a favoritos",Snackbar.LENGTH_SHORT).show()

        }

    }

}


