package com.example.mastergame.funcionesSecundarias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mastergame.R
import com.example.mastergame.databinding.ActivityNoticiasBinding
import com.example.mastergame.db.ArticulosDatabase
import com.example.mastergame.funcionesPrincipales.ExplorerActivity
import com.example.mastergame.funcionesPrincipales.PerfilActivity
import com.example.mastergame.funcionesSecundarias.fragmentos.Buscador_Noticias_Fragment
import com.example.mastergame.repository.NoticiasRepositorio
import com.example.mastergame.util.UiUtil
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Noticias : AppCompatActivity() {
    lateinit var newsViewModel: NewsViewModel
    lateinit var binding:ActivityNoticiasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNoticiasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.menuTopExplorer

        val noticiasRepositorio = NoticiasRepositorio(ArticulosDatabase(this))
        val viewModelProviderFactory = NoticiasViewModelProviderFactory(application, noticiasRepositorio)
        newsViewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        binding.menuImage.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // menu lateral
        navigationView.setNavigationItemSelectedListener { menuIter ->
            when (menuIter.itemId) {
                R.id.categorias -> {
                    val intent = Intent(this, ExplorerActivity::class.java)
                    intent.putExtra("profile_user_id",FirebaseAuth.getInstance().currentUser?.uid)
                    startActivity(intent)
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

        }
    }
