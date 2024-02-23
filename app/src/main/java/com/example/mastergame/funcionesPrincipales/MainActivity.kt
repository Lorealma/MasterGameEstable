package com.example.mastergame.funcionesPrincipales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mastergame.R
import com.example.mastergame.adapter.VideoListAdapter
import com.example.mastergame.databinding.ActivityMainBinding
import com.example.mastergame.model.UserModel
import com.example.mastergame.model.VideoModel
import com.example.mastergame.util.UiUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class MainActivity : AppCompatActivity() {
  lateinit var binding: ActivityMainBinding
  lateinit var adapter: VideoListAdapter


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)


    binding.ivComentario.setOnClickListener {
      val intent= Intent(this, ComentariosActivity:: class.java)
      startActivity(intent)
    }


    //iteracion por el menu
    binding.bottomNavBar.setOnItemSelectedListener() { menuItem ->
      when (menuItem.itemId) {
        R.id.bottom_menu_inicio -> {


        }

        R.id.bottom_menu_explorar -> {
          val intent = Intent(this, ExplorerActivity::class.java)
          intent.putExtra("profile_user_id", FirebaseAuth.getInstance().currentUser?.uid)
          startActivity(intent)

        }

        R.id.bottom_menu_añadir_video -> {

          startActivity(Intent(this, SubirVideoActivity::class.java))

          UiUtil.showToast(this, "Añadir video")


        }

        R.id.bottom_menu_retos -> {
          val intent = Intent(this, Social::class.java)
          intent.putExtra("profile_user_id", FirebaseAuth.getInstance().currentUser?.uid)
          startActivity(intent)

        }

        R.id.botton_menu_perfil -> {

          val intent = Intent(this, PerfilActivity::class.java)
          intent.putExtra("profile_user_id", FirebaseAuth.getInstance().currentUser?.uid)
          startActivity(intent)


        }

      }
      false

    }
    setupViewPager()
    binding.fav.setOnClickListener {
      val userId = FirebaseAuth.getInstance().currentUser!!.uid
      val videoId = adapter.getCurrentItemId()
      aniadirvideo(userId,videoId)
      Toast.makeText(this, "Añadido a favoritos", Toast.LENGTH_SHORT).show()
    }
  }

  //funcion para ver los videos en pantalla
  private fun setupViewPager() {
    val options = FirestoreRecyclerOptions.Builder<VideoModel>()
      .setQuery(
        Firebase.firestore.collection("videos"),
        VideoModel::class.java
//build sirve para llamar un objeto de la clase uri
      ).build()
    adapter = VideoListAdapter(options)
    binding.viewPager.adapter = adapter
  }
  private fun aniadirvideo(userId: String, videoId: String) {
    FirebaseFirestore.getInstance().collection("users").document(userId)
      .update("likes", FieldValue.arrayUnion(videoId))
      .addOnSuccessListener {

      }

  }
  override fun onStart() {
    super.onStart()
    adapter?.startListening()
  }
}


