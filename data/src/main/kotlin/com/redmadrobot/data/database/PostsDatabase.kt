package com.redmadrobot.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.redmadrobot.data.model.PostDbEntity
import com.redmadrobot.data.model.ProfileDbEntity

@Database(
    entities = [ProfileDbEntity::class, PostDbEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PostsDatabase : RoomDatabase() {

    abstract val postsDao: PostsDao
    abstract val favoritePostsDao: FavoritePostsDao
    abstract val friendsDao: FriendsDao

    companion object {
        fun getInstance(context: Context): PostsDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                PostsDatabase::class.java,
                "posts_database"
            ).build()
        }
    }
}
