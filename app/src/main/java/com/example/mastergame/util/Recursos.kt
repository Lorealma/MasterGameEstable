package com.example.mastergame.util




sealed class Recursos<T> (
     val data: T? = null,
     val message : String? = null
 ){
     class Success<T>(data: T): Recursos<T>(data)
    class Error<T>(message:String,data:T?=null):Recursos<T>(data,message)
    class Cargando<T>:Recursos<T>()

 }
