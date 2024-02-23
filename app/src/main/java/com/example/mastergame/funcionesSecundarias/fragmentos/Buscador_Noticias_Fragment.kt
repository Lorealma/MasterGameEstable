package com.example.mastergame.funcionesSecundarias.fragmentos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mastergame.R
import com.example.mastergame.adapter.NoticiasAdapter
import com.example.mastergame.databinding.FragmentBuscadorNoticiasBinding
import com.example.mastergame.databinding.FragmentFavoritasNoticiasFragmentosBinding
import com.example.mastergame.funcionesSecundarias.NewsViewModel
import com.example.mastergame.funcionesSecundarias.Noticias
import com.example.mastergame.util.ConstanteNoticias
import com.example.mastergame.util.ConstanteNoticias.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.mastergame.util.Recursos
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Buscador_Noticias_Fragment : Fragment(R.layout.fragment_buscador__noticias_) {
    lateinit var newsViewModel: NewsViewModel
    lateinit var noticiasAdapter: NoticiasAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemSearchError: CardView
    lateinit var binding: FragmentBuscadorNoticiasBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBuscadorNoticiasBinding.bind(view)

        itemSearchError = view.findViewById(R.id.itemSearchError)

        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_error, null)
        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        newsViewModel = (activity as Noticias).newsViewModel
        setupSearchRecylcer()

        noticiasAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_buscador_Noticias_Fragment_to_articulos_Fragmentos,
                bundle
            )

        }

        var job: Job? = null
        binding.searchEdit.addTextChangedListener() { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        newsViewModel.searchNews(editable.toString())
                    }
                }
            }
        }
        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer { response->
            when (response){
                is Recursos.Success<*>-> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let {noticiasResponse ->
                        noticiasAdapter.differ.submitList(noticiasResponse.articles.toList())
                        val totalPages = noticiasResponse.totalResults / ConstanteNoticias.QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.searNewsPage == totalPages
                        if(isLastPage){
                            binding.recyclerSearch.setPadding(0,0,0,0)
                        }
                    }
                }
                is Recursos.Error<*>-> {
                    hideProgressBar()
                    response.message?.let {messagge ->
                        Toast.makeText(activity, "lo siento somos noob: $messagge", Toast.LENGTH_LONG)
                        showErrorMessage(messagge)

                    }

                }
                is Recursos.Cargando<*>-> {
                    showProgressBar()

                }
            }
        })
        retryButton.setOnClickListener{
            if (binding.searchEdit.text.toString().isNotEmpty()){
                newsViewModel.searchNews(binding.searchEdit.text.toString())

            }else {
                hideErrorMessage()

            }
        }
    }


    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        itemSearchError.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemSearchError.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= ConstanteNoticias.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                newsViewModel.searchNews(binding.searchEdit.text.toString())
                isScrolling = false

            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }

        }
    }

    private fun setupSearchRecylcer() {
        noticiasAdapter = NoticiasAdapter()
        binding.recyclerSearch.apply {
            adapter = noticiasAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@Buscador_Noticias_Fragment.scrollListener)

        }

    }
}