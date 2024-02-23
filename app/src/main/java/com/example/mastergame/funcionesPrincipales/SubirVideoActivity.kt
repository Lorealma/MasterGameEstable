package com.example.mastergame.funcionesPrincipales

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.mastergame.databinding.ActivitySubirVideoBinding
import com.example.mastergame.model.CategoriasModel
import com.example.mastergame.model.VideoModel
import com.example.mastergame.util.UiUtil
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage


class SubirVideoActivity : AppCompatActivity() {

    lateinit var binding: ActivitySubirVideoBinding

    //Importamos la clase Uri que se usa para identificar recursos, en este caso video

    private var SelectedVideoUri: Uri? = null

    lateinit var videoLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubirVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

//lo que hacemos con video_launcher es iterarlo, si tiene un resultado lo guarda en la variable privada
        videoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    SelectedVideoUri = result.data?.data
                    UiUtil.showToast(this, "coje un video" + SelectedVideoUri.toString())
                    ShowPostView();

                }
              cargarCategorias()

            }

        //funcion de boton para añadir (el boton con el mas de la vista, que esta en el centro)

        binding.subidosView.setOnClickListener {
            checkPermissionAndOpenVideoPicker()

        }
        binding.publicarPublicacionBtn.setOnClickListener {
            val categoriaSeleccionada = binding.SpinnerCategoria.selectedItem.toString()
            postVideo(categoriaSeleccionada)
        }
        binding.cancelarPublicacionBtn.setOnClickListener {
            finish()
        }

    }
    //funcion para el boton subir video
    private fun cargarCategorias() {
      val categoriasList = mutableListOf<String>()

      Firebase.firestore.collection("category")
        .get()
        .addOnSuccessListener { result ->
          for (document in result) {
            // Para cada documento en la colección "category", agrega el nombre al listado
            val categoria = document.toObject(CategoriasModel::class.java)
            categoriasList.add(categoria.name)
          }
          val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriasList)
          adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
          binding.SpinnerCategoria.adapter = adapter
        }
        .addOnFailureListener { exception ->
          // Manejar errores al obtener las categorías
          UiUtil.showToast(applicationContext, "Error al cargar las categorías")
        }
    }

    private fun postVideo(categoriaSeleccionada: String) {
      val categoriaSeleccionada = binding.SpinnerCategoria.selectedItem.toString()

      if (binding.inputPublicacion.text.toString().isEmpty() || categoriaSeleccionada.isEmpty()) {
        binding.inputPublicacion.setError("¿Escribirás? Selecciona una categoría.")
        return
      }
        setinProgress(true)
        SelectedVideoUri?.apply {

            // Vamos a guardar el objeto en la base de datos Firebase
            val videoRef =
                FirebaseStorage.getInstance().reference.child("videos/" + this.lastPathSegment)
            videoRef.putFile(this)
                .addOnSuccessListener {
                    videoRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                        // Ver la referencia del video en la base de datos y descargarla
                        postToFirestore(downloadUrl.toString(), categoriaSeleccionada)

                    }
                }
                .addOnFailureListener {
                    setinProgress(false)
                    UiUtil.showToast(applicationContext, "Error al subir el video")
                }
        }

    }

    //funcion para subir los datos del video a firebase
    private fun postToFirestore(url: String, categoria: String) {
      val videoModel = VideoModel(
        videoId = FirebaseAuth.getInstance().currentUser?.uid!! + "_" + Timestamp.now().toString(),
        titulo = binding.inputPublicacion.text.toString(),
        url = url,
        uploaderId = FirebaseAuth.getInstance().currentUser?.uid!!,
        categoria = categoria,
        createdTime = Timestamp.now()
      )
      val videosCollection = Firebase.firestore.collection("videos")
      videosCollection.document(videoModel.videoId)
        .set(videoModel)
        .addOnSuccessListener {
          setinProgress(false)
          UiUtil.showToast(applicationContext, "Video Subido ")
          finish()
        }
        .addOnFailureListener {
          setinProgress(false)
          UiUtil.showToast(applicationContext, "Error al subir el video")
        }

      actualizarListaDeVideosEnCategoria(categoria, videoModel.videoId)
    }

  private fun actualizarListaDeVideosEnCategoria(categoria: String, videoId: String) {
    val categoriaDocument = Firebase.firestore.collection("category").document(categoria)
    categoriaDocument.update("videos", FieldValue.arrayUnion(videoId))
      .addOnSuccessListener {

      }
      .addOnFailureListener {

      }
  }
  //funcion para la barra de progreso
  fun setinProgress(inProgress : Boolean){
        if (inProgress){
            binding.barraCarga.visibility = View.VISIBLE
            binding.publicarPublicacionBtn.visibility = View.GONE

        }else{
            binding.barraCarga.visibility = View.GONE
            binding.publicarPublicacionBtn.visibility = View.VISIBLE

        }


    }

    //funcion para que se vea el video una vez cargado

    private fun ShowPostView(){

        SelectedVideoUri?.let{
            binding.publicacionView.visibility = View.VISIBLE
            binding.subidosView.visibility = View.GONE
            Glide.with(binding.vistaPublicacion).load(it).into(binding.vistaPublicacion)


        }

    }

    //Funcion para el videoPicker. implemetamos Build que viene de Uri y sirve para decirle el bojeto que queremos que sera uri, ademas la version code es la actualizacion de androi en este caso Tiramisu
    // Checkpermision, esta funcion nos permite acceder a los videos del movil
    private fun checkPermissionAndOpenVideoPicker(){
        var readExternalVideo: String = ""
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES. TIRAMISU){
            readExternalVideo = android.Manifest.permission.READ_MEDIA_VIDEO

    }else{
            readExternalVideo = android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(this,readExternalVideo)== PackageManager.PERMISSION_GRANTED){

            openVideoPicker()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(readExternalVideo),200)
        }

        }
    private fun openVideoPicker(){
        var intent = Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type= "video/*"
        videoLauncher.launch(intent)
    }
}
