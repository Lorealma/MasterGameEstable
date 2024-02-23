package com.example.mastergame.funcionesSecundarias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mastergame.R
import com.example.mastergame.adapter.FollowersAdapter
import com.example.mastergame.adapter.Followingadapter
import com.example.mastergame.databinding.ActivityFollowingListBinding
import com.example.mastergame.databinding.ActivityListaSeguidoresBinding
import com.example.mastergame.model.UserModel

class FollowingList : AppCompatActivity() {
    companion object{
        lateinit var users : UserModel
    }
    lateinit var binding: ActivityFollowingListBinding
    lateinit var followingadapter: Followingadapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupFollowersListRecycler()

    }
    fun setupFollowersListRecycler(){
        followingadapter = Followingadapter(users.followingList)
        binding.recyclerFollowing.layoutManager = LinearLayoutManager(this)
        binding.recyclerFollowing.adapter = followingadapter

    }

}