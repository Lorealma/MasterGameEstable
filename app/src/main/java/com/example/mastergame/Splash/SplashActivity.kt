package com.example.mastergame.Splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.mastergame.databinding.ActivitySplashBinding
import com.example.mastergame.funcionesPrincipales.EntrarActivity
import com.example.mastergame.funcionesPrincipales.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val splashDuration = 2000L  // Duración en milisegundos (2 segundos)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, EntrarActivity::class.java)
            startActivity(intent)
            finish()
        }, splashDuration)
    }
}