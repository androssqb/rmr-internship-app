package com.redmadrobot.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.redmadrobot.data.model.relations.PostAndProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePostsDao {

    @Transaction
    @Query("SELECT * FROM posts WHERE liked")
    fun fetchFavoritePostAndProfileList(): Flow<List<PostAndProfile>>

    @Transaction
    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun fetchFavoritePostAndProfile(id: String): PostAndProfile
}
