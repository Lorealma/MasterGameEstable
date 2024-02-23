package com.example.mastergame.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.mastergame.model.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)

abstract class ArticulosDatabase:RoomDatabase() {

    abstract fun getArticulosDao(): ArticulosDao

    companion object{
        @Volatile
        private var instance:ArticulosDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){ //posible fallo por el context
            instance ?: createDatabase(context).also{
                instance = it
            }

        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticulosDatabase::class.java,
                "article_db.db"
            ).build()
    }


}