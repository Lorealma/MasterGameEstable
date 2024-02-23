package com.example.mastergame.funcionesPrincipales

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mastergame.R
import com.example.mastergame.adapter.ProfileVideoAdapter
import com.example.mastergame.databinding.ActivityPerfilBinding
import com.example.mastergame.funcionesSecundarias.FollowingList
import com.example.mastergame.funcionesSecundarias.Lista_Seguidores
import com.example.mastergame.model.UserModel
import com.example.mastergame.model.VideoModel
import com.example.mastergame.util.UiUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query

import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

class PerfilActivity : AppCompatActivity() {

    lateinit var binding: ActivityPerfilBinding
    lateinit var profileUserId : String
    lateinit var currentUserId : String
    lateinit var profileUserModel: UserModel
    lateinit var photoLauncher: ActivityResultLauncher<Intent>
    lateinit var adapter: ProfileVideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileUserId = intent.getStringExtra("profile_user_id")!!
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid!!


        val drawerLayout: DrawerLayout = binding.drawerperfil
        val navigationView: NavigationView = binding.menuTopPerfil


        //ITERACIÓN MENÚ

        binding.bottomNavBar.setOnItemSelectedListener(){menuItem->
            when(menuItem.itemId){
                R.id.bottom_menu_inicio ->{
                    val intent= Intent(this, MainActivity:: class.java)
                    intent.putExtra("profile_user_id",FirebaseAuth.getInstance().currentUser?.uid)
                    startActivity(intent)

                }
                R.id.bottom_menu_explorar ->{
                    val intent= Intent(this, ExplorerActivity:: class.java)
                    intent.putExtra("profile_user_id",FirebaseAuth.getInstance().currentUser?.uid)
                    startActivity(intent)

                }
                R.id.bottom_menu_añadir_video ->{

                    startActivity(Intent(this, SubirVideoActivity::class.java))


                }
                R.id.bottom_menu_retos ->{
                    val intent = Intent(this, Social::class.java)
                    intent.putExtra("profile_user_id",FirebaseAuth.getInstance().currentUser?.uid)
                    startActivity(intent)

                }
                R.id.botton_menu_perfil ->{

                    val intent= Intent(this, PerfilActivity:: class.java)
                    intent.putExtra("profile_user_id",FirebaseAuth.getInstance().currentUser?.uid)
                    startActivity(intent)


                }

            }
            false

        }


        //ABRIR NAVEGADOR LATERAL

        binding.menuImage.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        //MENU LATERAL
        navigationView.setNavigationItemSelectedListener { menuIter ->
            when (menuIter.itemId) {
                R.id.editarPerfil-> {

                    val intent= Intent(this, EditarPerfilActivity:: class.java)
                    intent.putExtra("profile_user_id",FirebaseAuth.getInstance().currentUser?.uid)
                    startActivity(intent)

                }
                R.id.privacidad -> {

                    val intent= Intent(this, PrivacidadActivity:: class.java)
                    intent.putExtra("profile_user_id",FirebaseAuth.getInstance().currentUser?.uid)
                    startActivity(intent)

                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }



        binding.followers.setOnClickListener{
            Lista_Seguidores.users = profileUserModel
            val intent = Intent(this, Lista_Seguidores::class.java)
            intent.putExtra("profile_user_id",FirebaseAuth.getInstance().currentUser?.uid)
            startActivity(intent)
        }


        binding.following.setOnClickListener{
            FollowingList.users = profileUserModel
            val intent = Intent(this, FollowingList::class.java)
            intent.putExtra("profile_user_id",FirebaseAuth.getInstance().currentUser?.uid)
            startActivity(intent)
        }


        photoLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode == RESULT_OK){
                uploadToFirestore(result.data?.data!!)

            }

        }

        if (profileUserId==currentUserId){

            //actual perfil
            binding.profileBtn.text="Logout"
            binding.profileBtn.setOnClickListener {
                logout()
            }
            binding.profilePic.setOnClickListener{

                checkPermissionAndPickPhoto()

            }

        }else{
            //seguir a otros usuarios
            binding.profileBtn.text ="Sigueme"
            binding.profileBtn.setOnClickListener {
                followUnfollowUser()
            }
        }
        getProfileDataFromFirebase()
        setupRecycleView()
    }

//funcion seguir a usuarios
    fun followUnfollowUser(){
    Firebase.firestore.collection("users")
        .document(currentUserId)
        .get()
        .addOnSuccessListener {
            val currentUserModel = it.toObject(UserModel::class.java)!!
            if(profileUserModel.followerList.contains(currentUserId)){
                //esto es dejar de seguir
                profileUserModel.followerList.remove(currentUserId)
                currentUserModel.followingList.remove(profileUserId)
                binding.profileBtn.text = "Follow"
            }else{
                // seguir a usuarios
                profileUserModel.followerList.add(currentUserId)
                currentUserModel.followingList.add(profileUserId)
                binding.profileBtn.text= "unfollow"
            }
            updateUserData(profileUserModel)
            updateUserData(currentUserModel)
        }
    }
    //funcion para actualizar lista de seguidores
    fun updateUserData(model: UserModel){
        Firebase.firestore.collection("users")
            .document(model.id)
            .set(model)
            .addOnSuccessListener {
                getProfileDataFromFirebase()
            }

    }

    //cargar imagen en la base de datos
    fun uploadToFirestore(photoUri: Uri){
        binding.barraProgreso.visibility= View.VISIBLE
        val photoRef =
            FirebaseStorage.getInstance().reference.child("profilePic/" + currentUserId)
        photoRef.putFile(photoUri)
            .addOnSuccessListener {
                photoRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    //ver la referencia del video en la base de datos y descargarla

                    postToFirestore(downloadUrl.toString())

                }
            }
    }
    //actualizar foto de perfil de base de datos
    fun postToFirestore(url : String) {
        Firebase.firestore.collection("users")
            .document(currentUserId)
            .update("profilePic", url)
            .addOnSuccessListener {
                getProfileDataFromFirebase()

            }


    }
    //funcion para conectar con la base de datos
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

    }

    fun openPhotoPicker(){
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type= "image/*"
        photoLauncher.launch(intent)
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, PerfilActivity :: class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }



    fun  getProfileDataFromFirebase () {
        Firebase.firestore.collection("users")
            .document(profileUserId)
            .get()
            .addOnSuccessListener {
                profileUserModel = it.toObject(UserModel::class.java)!!
                setUi()
            }

    }
    //funcion para mostrar datos tanto de foto de perfil comop seguidores, post y seguidos
    fun setUi(){
        profileUserModel.apply {
            Glide.with(binding.profilePic).load(profilePic)
                .apply(RequestOptions().placeholder(R.drawable.icon_profile))
                .circleCrop() // hace la foto de perfil se vea redondita

                .into(binding.profilePic)
            binding.perfilApodo.text = "@" + username
            if(profileUserModel.followerList.contains(currentUserId))
                binding.profileBtn.text = "unfollow"
            binding.barraProgreso.visibility= View.INVISIBLE
            binding.followingCuenta.text = followingList.size.toString()
            binding.unfollowingCuenta.text= followerList.size.toString() //no seria unfollowing seria follower, hay que cambiar el id en el xml
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
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.startListening()

    }



}
