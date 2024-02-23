package com.example.mastergame.funcionesPrincipales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mastergame.R
import com.example.mastergame.api.KeyConstanChat
import com.example.mastergame.databinding.ActivitySocialBinding
import com.example.mastergame.databinding.ItemFollowerBinding
import com.example.mastergame.funcionesSecundarias.Corversacion
import com.example.mastergame.util.UiUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zegocloud.zimkit.services.ZIMKit
import im.zego.zim.enums.ZIMErrorCode
class Social : AppCompatActivity() {
  private lateinit var binding: ActivitySocialBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySocialBinding.inflate(layoutInflater)
    setContentView(binding.root)
    initZegocloud()
    binding.bottomNavBar.setOnItemSelectedListener { menuItem ->
      when (menuItem.itemId) {
        R.id.bottom_menu_inicio -> {
          val intent = Intent(this, MainActivity::class.java)
          intent.putExtra("profile_user_id", FirebaseAuth.getInstance().currentUser?.uid)
          startActivity(intent)
        }
        R.id.bottom_menu_explorar -> {
          val intent = Intent(this, ExplorerActivity::class.java)
          intent.putExtra("profile_user_id", FirebaseAuth.getInstance().currentUser?.uid)
          startActivity(intent)
        }
        R.id.bottom_menu_añadir_video -> {
          startActivity(Intent(this, SubirVideoActivity::class.java))
        }
        R.id.bottom_menu_retos -> {
          // No está claro qué deseas hacer aquí, ya que inicias la misma actividad nuevamente
        }
        R.id.botton_menu_perfil -> {
          val intent = Intent(this, PerfilActivity::class.java)
          intent.putExtra("profile_user_id", FirebaseAuth.getInstance().currentUser?.uid)
          startActivity(intent)
        }
      }
      false
    }

      binding.chat.setOnClickListener {
        val intent = Intent(this@Social, Corversacion::class.java)
        chat().obtenerdatos()
        startActivity(intent)

      }

  }
  fun initZegocloud(){
    ZIMKit.initWith(this.application, KeyConstanChat.appID, KeyConstanChat.appSing)
    ZIMKit.initNotifications()

  }



  inner class chat {


    //rellenar los valores necesarios para el chat
    fun obtenerdatos() {
      val auth = FirebaseAuth.getInstance()
      val userId = auth.currentUser?.uid
      if (userId != null) {
        val db = FirebaseFirestore.getInstance()
        val datos = db.collection("users").document(userId)
        datos.get()
          .addOnSuccessListener { document ->
            if (document != null) {
              val userId = document.getString("username") ?: ""
              val userAvatar = document.getString("profilePic") ?: ""
              connectUser(userId, userId, userAvatar)
            }
          }
      }
    }

    fun connectUser(userId: String, userName: String, userAvatar: String) {
      ZIMKit.connectUser(userId, userName, userAvatar) { errorInfo ->
        if (errorInfo.code == ZIMErrorCode.SUCCESS) {

        }

      }

    }



  }
}







