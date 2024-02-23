package com.example.mastergame.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mastergame.R
import com.example.mastergame.databinding.ItemFollowingBinding
import com.example.mastergame.funcionesPrincipales.PerfilActivity
import com.example.mastergame.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore

class Followingadapter (private val followingList : MutableList<String>) :
    RecyclerView.Adapter<Followingadapter.FollowingViewHolder>(){

        inner class FollowingViewHolder(private val binding: ItemFollowingBinding) :
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
            val binding = ItemFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FollowingViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return followingList.size
        }

        override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
            holder.bindData(followingList[position])
        }
    }
