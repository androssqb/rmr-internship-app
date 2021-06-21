package com.redmadrobot.data.database

import androidx.room.*
import com.redmadrobot.data.model.PostDbEntity
import com.redmadrobot.data.model.ProfileDbEntity
import com.redmadrobot.data.model.relations.PostAndProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPosts(postList: List<PostDbEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProfiles(profiles: List<ProfileDbEntity>)

    @Transaction
    suspend fun insertPostAndProfileList(postList: List<PostDbEntity>, profiles: List<ProfileDbEntity>) {
        insertAllPosts(postList = postList)
        insertAllProfiles(profiles = profiles)
    }

    @Transaction
    suspend fun insertPostAndProfile(post: PostDbEntity, profile: ProfileDbEntity) {
        insertPost(post = post)
        insertProfile(profile = profile)
    }

    @Transaction
    @Query("SELECT * FROM posts")
    fun fetchPostAndProfileList(): Flow<List<PostAndProfile>>

    @Transaction
    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun fetchPostAndProfile(id: String): PostAndProfile

    @Query("UPDATE posts SET liked = :liked WHERE id = :id ")
    suspend fun changePostLike(id: String, liked: Boolean)

    @Query("SELECT * FROM posts WHERE author_id = :authorId")
    suspend fun fetchAllAuthorsPosts(authorId: String): List<PostDbEntity>
}
