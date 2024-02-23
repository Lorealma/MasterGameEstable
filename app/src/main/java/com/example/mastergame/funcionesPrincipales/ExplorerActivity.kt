package com.example.mastergame.funcionesPrincipales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mastergame.funcionesSecundarias.Noticias
import com.example.mastergame.R
import com.example.mastergame.adapter.CategoriasAdapter
import com.example.mastergame.databinding.ActivityExplorerInicioBinding
import com.example.mastergame.model.CategoriasModel
import com.example.mastergame.util.UiUtil
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ExplorerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExplorerInicioBinding
    private lateinit var categoriasAdapter: CategoriasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExplorerInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.menuTopExplorer

        // menu inferior
        binding.bottomNavBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_menu_inicio -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("profile_user_id", FirebaseAuth.getInstance().currentUser?.uid)
                    startActivity(intent)
                }

                R.id.bottom_menu_explorar -> {
                    // Mantente en la actividad actual o realiza la acción deseada
                }

                R.id.bottom_menu_añadir_video -> {
                    startActivity(Intent(this, SubirVideoActivity::class.java))

                }

                R.id.bottom_menu_retos -> {
                    val intent = Intent(this, Social::class.java)
                    intent.putExtra("profile_user_id",FirebaseAuth.getInstance().currentUser?.uid)
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

        // abre la navegacion lateral
        binding.menuImage.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // menu lateral
        navigationView.setNavigationItemSelectedListener { menuIter ->
            when (menuIter.itemId) {
                R.id.categorias -> {
                    // Mantente en la actividad actual o realiza la acción deseada
                }
                R.id.Noticias -> {
                    val intent = Intent(this, Noticias::class.java)
                    intent.putExtra("profile_user_id", FirebaseAuth.getInstance().currentUser?.uid)
                    startActivity(intent)
                }
                R.id.calendario -> {
                    UiUtil.showToast(this, "calendario")
                }
                R.id.tu_lista -> {
                    UiUtil.showToast(this, "tu lista")
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Configuración del RecyclerView de categorías
        getCategorias()
    }

    private fun getCategorias() {
        FirebaseFirestore.getInstance().collection("category")
            .get().addOnSuccessListener {
                val categoryList = it.toObjects(CategoriasModel::class.java)
                setupCategoriaRecyclerView(categoryList)
            }
    }

    private fun setupCategoriaRecyclerView(categoryList: List<CategoriasModel>) {
        categoriasAdapter = CategoriasAdapter(categoryList)
        binding.categoriasRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriasRecyclerView.adapter = categoriasAdapter
    }
}

