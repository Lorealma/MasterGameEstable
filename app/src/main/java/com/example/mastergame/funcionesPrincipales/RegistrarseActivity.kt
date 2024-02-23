package com.example.mastergame.funcionesPrincipales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.example.mastergame.databinding.ActivityRegistrarseBinding
import com.example.mastergame.model.UserModel
import com.example.mastergame.util.UiUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class RegistrarseActivity : AppCompatActivity() {

    //metemos la variable binding que es para las view, ademas el lateinti indica que se iniciara mas tarde

    lateinit var binding: ActivityRegistrarseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegistrarseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Boton para meter datos, por eso el submit

        binding.submitBoton.setOnClickListener {
            singup()
        }

        //Boton para ir a otra view por eso go to

        binding.goToEntrarBtn.setOnClickListener {
            startActivity(Intent(this, EntrarActivity::class.java))
            finish()

        }
    }

    // funcion para la barra de progreso

    fun setinProgress(inProgress : Boolean){
        if (inProgress){
            binding.progressBar.visibility = View.VISIBLE
            binding.submitBoton.visibility = View.GONE

        }else{
            binding.progressBar.visibility = View.GONE
            binding.submitBoton.visibility = View.VISIBLE

        }
    }

    //funciones para confirma contraseña y correo correcto

fun singup(){
    val email= binding.emailInput.text.toString()
    val password= binding.contrasenaInput.text.toString()
    val confirmpassword= binding.confirmarContrasenaInput.text.toString()

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

        binding.emailInput.setError("El email no es valido")
        return ;

    }


    if (password.length<10) {
        binding.contrasenaInput.setError("La contraseña minimo tiene que tener 10 caracteres")
        return;
    }

    if (password!=confirmpassword){
        binding.confirmarContrasenaInput.setError("La contraseña no coincide")
        return;

    }
    singupWithFirebase(email, password)

}
    //creamos la funcion para conectar con firebase

    fun singupWithFirebase( email: String, password: String ){

        setinProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email,password

        ).addOnSuccessListener {
            it.user?.let {user->
                val userModel= UserModel (user.uid,email,email.substringBefore("@"),"",
                    mutableListOf(), mutableListOf(), mutableListOf()
                )
                Firebase.firestore.collection("users")
                    .document(user.uid)
                    .set(userModel).addOnSuccessListener {
                        UiUtil.showToast(applicationContext, "Se ha creado la cuenta")
                        setinProgress(false)
                        startActivity(
                            Intent(this, MainActivity::class.java)
                        )
                        finish()
                    }

            }

        }.addOnFailureListener(){
            UiUtil.showToast(applicationContext,it.localizedMessage ?:"Algo a fallado")
            setinProgress(false)
        }

    }



}




