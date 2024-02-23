package com.example.mastergame.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mastergame.R
import com.example.mastergame.model.Comentario

class ComentarioAdapter(private val listaComentarios:List<Comentario>): RecyclerView.Adapter<ComentarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return ComentarioViewHolder(layoutInflater.inflate(R.layout.item_comentario_subido, parent, false))


    }

    override fun getItemCount(): Int {

        return listaComentarios.size

    }

    override fun onBindViewHolder(holder: ComentarioViewHolder, position: Int) {

        val item = listaComentarios[position]
        holder.render(item)

    }

}