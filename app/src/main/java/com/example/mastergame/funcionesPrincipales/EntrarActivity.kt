package com.example.mastergame.funcionesPrincipales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.example.mastergame.databinding.ActivityEntrarBinding

import com.example.mastergame.model.UserModel
import com.example.mastergame.util.UiUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class EntrarActivity : AppCompatActivity() {

    lateinit var binding: ActivityEntrarBinding
    lateinit var firestore: FirebaseFirestore
    lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEntrarBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //mantener al usuario conectado

        FirebaseAuth.getInstance().currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

        binding.submitBoton.setOnClickListener {
            entrar()
        }

        //Boton para ir a otra view por eso go to

        binding.goToRegistrarseBtn.setOnClickListener {
            startActivity(Intent(this, RegistrarseActivity::class.java))
            finish()

        }

        // funcion para la barra de progreso

    }

  fun setinProgress(inProgress : Boolean){
        if (inProgress){
            binding.progressBar.visibility = View.VISIBLE
            binding.submitBoton.visibility = View.GONE

        }else{
            binding.progressBar.visibility = View.GONE
            binding.submitBoton.visibility = View.VISIBLE

        }

        //funciones para confirma contraseña y correo correcto

    }
    fun  entrar (){
        val email= binding.emailInput.text.toString()
        val password= binding.contrasenaInput.text.toString()


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            binding.emailInput.setError("El email no es valido")
            return ;

        }


        if (password.length<10) {
            binding.contrasenaInput.setError("La contraseña minimo tiene que tener 10 caracteres")
            return;
        }

        entrarWithFirebase(email,password)


    }

    fun entrarWithFirebase(email:String, password: String){
        setinProgress(true)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email,
            password
        ).addOnSuccessListener {
            UiUtil.showToast(this,"has entrado correctamente")
            setinProgress(false)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }.addOnFailureListener{
            UiUtil.showToast(applicationContext,it.localizedMessage?:("no has podido entrar"))
            setinProgress(false)

        }




    }

}
