package com.example.mastergame.funcionesSecundarias


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mastergame.adapter.FollowersAdapter
import com.example.mastergame.databinding.ActivityListaSeguidoresBinding
import com.example.mastergame.model.UserModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class Lista_Seguidores : AppCompatActivity() {
  companion object{
    lateinit var users : UserModel
  }
  lateinit var binding: ActivityListaSeguidoresBinding
  lateinit var followersAdapter: FollowersAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityListaSeguidoresBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupFollowersListRecycler()

  }
  fun setupFollowersListRecycler(){
    followersAdapter = FollowersAdapter(users.followerList)
    binding.recyclerFollowers.layoutManager = LinearLayoutManager(this)
    binding.recyclerFollowers.adapter = followersAdapter

  }
}
