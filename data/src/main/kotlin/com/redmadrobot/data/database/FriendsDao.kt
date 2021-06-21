package com.redmadrobot.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.redmadrobot.data.model.ProfileDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao {

    @Query("SELECT * from profiles WHERE id NOT LIKE :userId")
    fun getAllFriends(userId: String): Flow<List<ProfileDbEntity>>

    @Query("SELECT * from profiles WHERE id = :id")
    suspend fun getFriend(id: String): ProfileDbEntity

    @Query("DELETE from profiles WHERE id = :id")
    suspend fun deleteProfile(id: String)

    @Query("SELECT EXISTS (SELECT * FROM profiles WHERE id = :id)")
    suspend fun isAlreadyFriend(id: String): Boolean

    @Query("DELETE FROM posts WHERE author_id = :authorId")
    suspend fun deleteAuthorPosts(authorId: String)

    @Transaction
    suspend fun deleteProfileAndPosts(profileId: String) {
        deleteAuthorPosts(authorId = profileId)
        deleteProfile(id = profileId)
    }
}
