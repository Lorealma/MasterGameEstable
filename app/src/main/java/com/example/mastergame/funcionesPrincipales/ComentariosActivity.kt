package com.example.mastergame.funcionesPrincipales

import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mastergame.R
import com.example.mastergame.adapter.ComentarioAdapter
import com.example.mastergame.databinding.ActivityComentariosBinding
import com.example.mastergame.model.ComentarioProvider
import com.example.mastergame.model.UserModel

class ComentariosActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityComentariosBinding
    private lateinit var boton: ImageView
    private lateinit var comentario: RelativeLayout
    lateinit var currentUserId : String
    lateinit var profileUserModel: UserModel


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComentariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.background,null)))


        boton = binding.ivBtnComentario
        comentario = binding.relativeComentario

        boton.setOnClickListener(this)
        //comentario.setOnClickListener(this)

    }

    private fun initRecyclerView(){

        binding.recyclerComentarios.layoutManager = LinearLayoutManager(this)
        binding.recyclerComentarios.adapter = ComentarioAdapter(ComentarioProvider.listaComentarios)

    }


    //VISIBILIDAD CAMPO ESCRIBIR COMENTARIO

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.iv_btn_comentario -> {
                boton.visibility = View.GONE
                comentario.visibility = View.VISIBLE
            }

        }

    }


    //cargar imagen en la base de datos
    /*fun uploadToFirestore(photoUri: Uri){
        //binding.barraProgreso.visibility= View.VISIBLE
        val photoRef =
            FirebaseStorage.getInstance().reference.child("profilePic/" + currentUserId)
        photoRef.putFile(photoUri)
            .addOnSuccessListener {
                photoRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    //ver la referencia del video en la base de datos y descargarla

                    postToFirestore(downloadUrl.toString())

                }
            }
    }*/

    /*//funcion para conectar con la base de datos
    fun checkPermissionAndPickPhoto(){
        var readExternalPhoto: String = ""
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES. TIRAMISU){
            readExternalPhoto = android.Manifest.permission.READ_MEDIA_IMAGES

        }else{
            readExternalPhoto = android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(this,readExternalPhoto)== PackageManager.PERMISSION_GRANTED){

            openPhotoPicker()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(readExternalPhoto),100)
        }

    }*/


    //FUNCION PARA MOSTRAR DATOS TANTO DE FOTO DE PERFIL COMO SEGUIDORES, POST Y SEGUIDOS

    /*fun setUi(){

        profileUserModel.apply {
            Glide.with(binding.).load(profilePic)
                .apply(RequestOptions().placeholder(R.drawable.icon_profile))
                .circleCrop() // hace la foto de perfil se vea redondita

                .into(binding.profilePic)
            binding.perfilApodo.text = "@" + username
            if(profileUserModel.followerList.contains(currentUserId))
                binding.profileBtn.text = "unfollow"
            binding.barraProgreso.visibility= View.INVISIBLE
            //binding.followingCuenta.text = followingList.size.toString()
            //binding.unfollowingCuenta.text= followerList.size.toString() //no seria unfollowing seria follower, hay que cambiar el id en el xml
            Firebase.firestore.collection("videos")
                .whereEqualTo("uploaderId",profileUserId)
                .get().addOnSuccessListener{
                    binding.postCuenta.text = it.size().toString()
                }

        }

    }

    fun setupRecycleView(){

        val options = FirestoreRecyclerOptions.Builder<VideoModel>()
            .setQuery(
                Firebase.firestore.collection("videos")
                    .whereEqualTo("uploaderId",profileUserId)
                    .orderBy( "createdTime", Query.Direction.DESCENDING),
                VideoModel::class.java
            ).build()
        adapter= ProfileVideoAdapter(options)
        binding.recyclerView.layoutManager= GridLayoutManager(this,3)
        binding.recyclerView.adapter= adapter

    }*/


    /*////// fun entrarWithFirebase(comentario:String, password: String){
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

         }*/




}