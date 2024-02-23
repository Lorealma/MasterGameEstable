package com.example.mastergame.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mastergame.funcionesPrincipales.PerfilActivity
import com.example.mastergame.R
import com.example.mastergame.databinding.VideoItemRowBinding
import com.example.mastergame.model.UserModel
import com.example.mastergame.model.VideoModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class VideoListAdapter(
    options: FirestoreRecyclerOptions<VideoModel>
): FirestoreRecyclerAdapter<VideoModel,VideoListAdapter.VideoViewHolder>(options) {
  private var currentVideoId: String = ""


    inner class VideoViewHolder(private val binding: VideoItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindVideo(videoModel: VideoModel){
          currentVideoId= videoModel.videoId


            //bind de los datos de usuario, es decir, lo que sale debajo del video
            Firebase.firestore.collection("users")
                .document(videoModel.uploaderId)
                .get().addOnSuccessListener {

                    val userModel = it?.toObject(UserModel :: class.java)
                    userModel?.apply {
                        binding.nombreUsuario.text=username


                        //aqui ira la foto
                        Glide.with(binding.perfilIcon).load(profilePic)
                            .circleCrop()
                            .apply(
                                RequestOptions().placeholder(R.drawable.icon_profile)
                            )
                            .into(binding.perfilIcon)

                        //actualizacion de la foto del main (sin esta funcion, al loguearse otra persona mantendria la foto de otro usuario)

                        binding.userDetailLayout.setOnClickListener {
                            val intent= Intent(binding.userDetailLayout.context, PerfilActivity:: class.java)
                            intent.putExtra("profile_user_id",id)
                            binding.userDetailLayout.context.startActivity(intent)

                        }
                    }
                }

            binding.captionView.text=videoModel.titulo //posible error aqui.
            binding.barraProgreso.visibility= View.VISIBLE

            //bindVideo
            binding.videoView.apply {
                setVideoPath(videoModel.url)
                setOnPreparedListener{
                    binding.barraProgreso.visibility= View.GONE
                    it.start()
                    it.isLooping = true
                }

                // el pause del video

                setOnClickListener{
                    if(isPlaying){
                        pause()
                        binding.pausarIcon.visibility= View.VISIBLE

                    }else{
                        start()
                        binding.pausarIcon.visibility=View.GONE
                    }
                }
            }
        }


    }
  fun getCurrentItemId(): String {
    return currentVideoId
  }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = VideoItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int, model: VideoModel) {
        holder.bindVideo(model)

    }


}
