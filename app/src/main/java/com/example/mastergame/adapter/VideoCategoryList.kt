package com.example.mastergame.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mastergame.R
import com.example.mastergame.databinding.VideoListaRecyclerRowBinding
import com.example.mastergame.funcionesPrincipales.PerfilActivity
import com.example.mastergame.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class VideoCategoryList(
    options: FirestoreRecyclerOptions<VideoModel>,
    private val videoIds: MutableList<String>
) : FirestoreRecyclerAdapter<VideoModel, VideoCategoryList.VideoViewHolder>(options) {

    class VideoViewHolder(private val binding: VideoListaRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(videoModel: VideoModel, videoId: String) {
            FirebaseFirestore.getInstance().collection("videos")
                .document(videoId).get()
                .addOnSuccessListener { videoSnapshot ->
                    val video = videoSnapshot.toObject(VideoModel::class.java)
                    video?.apply {
                        binding.tiutloVideo.text = titulo

                        // Obtener el nombre de usuario desde la colección de usuarios
                        FirebaseFirestore.getInstance().collection("users")
                            .document(uploaderId).get()
                            .addOnSuccessListener { userSnapshot ->
                                val userName = userSnapshot.getString("username")
                                binding.subtiutloVideo.text = userName
                                val profilePic = userSnapshot.getString("profilePic")
                                Glide.with(binding.perfilIcon).load(profilePic)
                                    .circleCrop()
                                    .apply(
                                        RequestOptions().placeholder(R.drawable.icon_profile)
                                    )
                                    .into(binding.perfilIcon)


                                    binding.videoView.apply {
                                        setVideoPath(videoModel.url)
                                        setOnPreparedListener {
                                            binding.barraProgreso.visibility = View.GONE
                                            it.start()
                                            it.isLooping = true
                                        }


                                        // Pausar el video
                                        setOnClickListener {
                                            if (isPlaying) {
                                                pause()
                                                binding.pausarIcon.visibility = View.VISIBLE
                                            } else {
                                                start()
                                                binding.pausarIcon.visibility = View.GONE
                                            }
                                        }
                                    }
                                val id = userSnapshot.getString("id")

                                binding.userDetailLayout.setOnClickListener {
                                    val intent = Intent(
                                        binding.userDetailLayout.context,
                                        PerfilActivity::class.java
                                    )
                                    intent.putExtra("profile_user_id", id)
                                    binding.userDetailLayout.context.startActivity(intent)
                                }
                            }
                    }
                }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding =
            VideoListaRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int, model: VideoModel) {
        // Pasa el modelo y el videoId al método bindData
        val videoId = videoIds[position]
        holder.bindData(model, videoId)}}



