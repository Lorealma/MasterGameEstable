package com.example.mastergame.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mastergame.R
import com.example.mastergame.databinding.ItemFollowerBinding
import com.example.mastergame.funcionesPrincipales.PerfilActivity
import com.example.mastergame.funcionesSecundarias.Lista_Seguidores
import com.example.mastergame.model.UserModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore



class FollowersAdapter(private val followerList : MutableList<String>) :
    RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>(){

    inner class FollowersViewHolder(private val binding: ItemFollowerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(id: String) {

            FirebaseFirestore.getInstance().collection("users").document(id).get()
                .addOnSuccessListener {
                    val user = it.toObject(UserModel::class.java)
                    user?.apply {
                        binding.nombreUsuario.text = username
                        Glide.with(binding.perfilIcon).load(profilePic)
                            .circleCrop()
                            .apply(
                                RequestOptions().placeholder(R.drawable.icon_profile)
                            )
                            .into(binding.perfilIcon)
                    }

                }


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        val binding = ItemFollowerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return followerList.size
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bindData(followerList[position])
    }
}



