package com.example.mastergame.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.mastergame.funcionesPrincipales.ListaVideosActivity


import com.example.mastergame.databinding.ActivityExplorerItemRecyclerBinding
import com.example.mastergame.model.CategoriasModel

class CategoriasAdapter(private val categoryList: List<CategoriasModel>) :
    RecyclerView.Adapter<CategoriasAdapter.MyViewHolder>() {


    //para el recycleview de categorias
    class MyViewHolder(private val binding : ActivityExplorerItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindData(category : CategoriasModel){
            binding.textCategoria.text = category.name
            Glide.with(binding.imagenCategoria).load(category.coverUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(32))
                )
                .into(binding.imagenCategoria)
            //cargar videos, posible error
            Log.i("videos",category.videos.size.toString())

            // empieza la actividad de lista de videos en uanto pinchas una categoria
            val context= binding.root.context
            binding.root.setOnClickListener{
                ListaVideosActivity.category = category
                context.startActivity(Intent(context, ListaVideosActivity :: class.java))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ActivityExplorerItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(categoryList[position])
    }
}
