package com.example.mastergame.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mastergame.model.Article

import androidx.room.Delete


@Dao
interface ArticulosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long
    @Query("SELECT * FROM articles")

    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle (article: Article)



}