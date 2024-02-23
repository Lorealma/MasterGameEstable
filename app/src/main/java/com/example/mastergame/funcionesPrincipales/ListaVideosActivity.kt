package com.example.mastergame.funcionesPrincipales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.mastergame.adapter.VideoCategoryList
import com.example.mastergame.databinding.ActivityListaVideosBinding
import com.example.mastergame.model.CategoriasModel
import com.example.mastergame.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListaVideosActivity : AppCompatActivity() {

    companion object {
        lateinit var category: CategoriasModel
    }

    private var adapter: VideoCategoryList? = null
    private lateinit var binding: ActivityListaVideosBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var videoCategoryList: VideoCategoryList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = Firebase.firestore

        binding.nameTextView.text = category.name
        Glide.with(binding.imagenCategoria).load(category.coverUrl)
            .apply(RequestOptions().transform(RoundedCorners(32)))
            .into(binding.imagenCategoria)

        setupVideoListRecyclerView()
    }

    private fun setupVideoListRecyclerView() {
        val videosRecyclerView = binding.videosListaRecyclerRow
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        videosRecyclerView.layoutManager = layoutManager

        // Suponiendo que category tiene la información de la categoría actual
        val videosQuery = firestore.collection("videos")
            .whereEqualTo("categoria", category.name)

        val options = FirestoreRecyclerOptions.Builder<VideoModel>()
            .setQuery(videosQuery, VideoModel::class.java)
            .build()

        videoCategoryList = VideoCategoryList(options, category.videos)
        adapter = videoCategoryList
        videosRecyclerView.adapter = adapter
    }


    override fun onStart() {
        super.onStart()
        // Asegurémonos de que el adaptador no sea nulo antes de accederlo
        adapter?.startListening()
    }

}