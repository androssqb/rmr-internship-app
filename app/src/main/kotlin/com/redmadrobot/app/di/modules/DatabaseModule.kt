package com.redmadrobot.app.di.modules

import android.content.Context
import com.redmadrobot.data.database.FavoritePostsDao
import com.redmadrobot.data.database.FriendsDao
import com.redmadrobot.data.database.PostsDao
import com.redmadrobot.data.database.PostsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providePostsDatabase(context: Context): PostsDatabase {
        return PostsDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun providePostsDao(postsDatabase: PostsDatabase): PostsDao {
        return postsDatabase.postsDao
    }

    @Singleton
    @Provides
    fun provideFavoritePostsDao(postsDatabase: PostsDatabase): FavoritePostsDao {
        return postsDatabase.favoritePostsDao
    }

    @Singleton
    @Provides
    fun provideFriendsDao(postsDatabase: PostsDatabase): FriendsDao {
        return postsDatabase.friendsDao
    }
}
