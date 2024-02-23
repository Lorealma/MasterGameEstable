package com.example.mastergame.util

import android.content.Context
import android.widget.Toast

//Es una clase obnject para anunciar que se ha creado correctamente, la clase toast sirve para dar mensajes cortos

object UiUtil {

    fun showToast(context : Context, message: String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()

    }
}