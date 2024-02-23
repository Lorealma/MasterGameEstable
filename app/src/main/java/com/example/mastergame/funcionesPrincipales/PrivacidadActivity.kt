package com.example.mastergame.funcionesPrincipales

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.mastergame.R
import com.example.mastergame.databinding.ActivityPrivacidadBinding

class PrivacidadActivity : AppCompatActivity() {
    lateinit var binding: ActivityPrivacidadBinding
    private lateinit var context: Context

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this

        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.background,null)))

        /*supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)

            // Cambiar el color de la flecha de retroceso si es necesario
            val upArrow = ContextCompat.getDrawable(context, R.drawable.logo)
            upArrow?.let {
                val wrapped = DrawableCompat.wrap(it)
                //DrawableCompat.setTint(wrapped, ContextCompat.getColor(context, R.color.black))
                setHomeAsUpIndicator(resizeImage(wrapped,40,40))

            }
        }*/

    }

    private fun resizeImage(image: Drawable, width: Int, height: Int): Drawable {
        val bitmap = (image as BitmapDrawable).bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        return BitmapDrawable(resources, scaledBitmap)
    }


    /*//MENÚ DE ARRIBA

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean{

        return when(item.itemId){

            R.id.home -> {
                home()
                return true
            }
            R.id.configuracion -> {
                configuracion()
                return true
            }
            R.id.login -> {
                login()
                return true
            }

            /*R.id.logout -> {
                logout()
                return true
            }*/

            else->super.onOptionsItemSelected(item)
        }

        return true
    }

    //FUNCIONES BOTONES MENÚ DE ARRIBA

    fun home(){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    public fun configuracion(){

        val intent = Intent(this, ConfiguracionActivity::class.java)
        startActivity(intent)

    }

    public fun login(){

        val intent = Intent(this, RegistrarseActivity::class.java)
        startActivity(intent)

    }*/

    /*public fun logout(){

        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, PerfilActivity :: class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }*/


}