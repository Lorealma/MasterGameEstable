package com.example.mastergame.funcionesSecundarias.fragmentos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mastergame.R
import com.example.mastergame.adapter.NoticiasAdapter
import com.example.mastergame.databinding.FragmentFavoritasNoticiasFragmentosBinding
import com.example.mastergame.funcionesSecundarias.NewsViewModel
import com.example.mastergame.funcionesSecundarias.Noticias
import com.google.android.material.snackbar.Snackbar


class Favoritas_noticias_Fragmentos : Fragment(R.layout.fragment_favoritas_noticias__fragmentos) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var noticiasAdapter: NoticiasAdapter
    lateinit var binding : FragmentFavoritasNoticiasFragmentosBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritasNoticiasFragmentosBinding.bind(view)
        newsViewModel = (activity as Noticias).newsViewModel
        setupFavouritesRecycler()

        noticiasAdapter.setOnItemClickListener {
            val bundle = Bundle ().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_favoritas_noticias_Fragmentos_to_articulos_Fragmentos,bundle)
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val article= noticiasAdapter.differ.currentList[position]
                newsViewModel.deleteArticle(article)
                Snackbar.make(view,"Borrado de favoritos",Snackbar.LENGTH_LONG).apply {
                    setAction ("Undo"){
                        newsViewModel.addToFavourites(article)
                    }
                    show()
                }

            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }
        newsViewModel.getFavoriteNews().observe(viewLifecycleOwner, Observer { articles->
            noticiasAdapter.differ.submitList(articles)
        })
    }
    private fun setupFavouritesRecycler(){
        noticiasAdapter = NoticiasAdapter()
        binding.recyclerFavourites.apply {
            adapter = noticiasAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }



}