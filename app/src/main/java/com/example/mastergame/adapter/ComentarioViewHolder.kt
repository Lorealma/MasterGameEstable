package com.example.mastergame.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mastergame.R
import com.example.mastergame.databinding.ItemComentarioSubidoBinding
import com.example.mastergame.funcionesPrincipales.PerfilActivity
import com.example.mastergame.model.Comentario
import com.example.mastergame.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ComentarioViewHolder(view: View): RecyclerView.ViewHolder(view){

    val binding = ItemComentarioSubidoBinding.bind(view)


    fun render(comentario: Comentario){

        binding.tvNombreUserComent.text = comentario.nombreUsuario
        binding.tvFecha.text = comentario.fecha
        binding.tvComentario.text = comentario.comentarioEscrito.toString()
        Glide.with(binding.ivUser.context).load(comentario.imagen).into(binding.ivUser)


        //bind de los datos de usuario, es decir, lo que sale debajo del video
       /* Firebase.firestore.collection("comentarios")
            .document(comentario.id_usuario)
            .get().addOnSuccessListener {

                val userModel = it?.toObject(UserModel :: class.java)
                userModel?.apply {
                    binding.tvNombreUserComent.text=username


                    //aqui ira la foto
                    Glide.with(binding.ivUser).load(profilePic)
                        .circleCrop()
                        .apply(
                            RequestOptions().placeholder(R.drawable.icon_profile)
                        )
                        .into(binding.ivUser)

                    //actualizacion de la foto del main (sin esta funcion, al loguearse otra persona mantendria la foto de otro usuario)

                    /*binding.userDetailLayout.setOnClickListener {
                        val intent= Intent(binding.userDetailLayout.context, PerfilActivity:: class.java)
                        intent.putExtra("profile_user_id",id)
                        binding.userDetailLayout.context.startActivity(intent)

                    }*/
                }
            }*/







    }

}