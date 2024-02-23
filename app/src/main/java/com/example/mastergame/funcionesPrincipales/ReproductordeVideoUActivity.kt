package com.example.mastergame.funcionesPrincipales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mastergame.adapter.VideoListAdapter
import com.example.mastergame.databinding.ActivityReproductordeVideoUactivityBinding
import com.example.mastergame.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

//clase para poder reproducir el video al pulsar uno en el perfil


class ReproductordeVideoUActivity : AppCompatActivity() {

    lateinit var binding: ActivityReproductordeVideoUactivityBinding
    lateinit var videoId: String
    lateinit var adapter: VideoListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReproductordeVideoUactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        videoId = intent.getStringExtra("videoId")!!
        setupViewPager()

    }

    fun setupViewPager(){
        val options = FirestoreRecyclerOptions.Builder<VideoModel>()
            .setQuery(
                Firebase.firestore.collection("videos")
                    .whereEqualTo("videoId",videoId),
                VideoModel::class.java
            ).build()
        adapter= VideoListAdapter(options)
        binding.videoPager.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }
}