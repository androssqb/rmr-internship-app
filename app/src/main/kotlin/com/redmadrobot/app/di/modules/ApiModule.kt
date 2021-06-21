package com.redmadrobot.app.di.modules

import com.redmadrobot.data.network.*
import com.redmadrobot.domain.di.Logout
import com.redmadrobot.domain.di.Main
import com.redmadrobot.domain.di.Mock
import com.redmadrobot.domain.di.Origin
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Suppress("TooManyFunctions")
@Module
class ApiModule {

    @Singleton
    @Provides
    @Origin
    fun providesAuthApi(@Main retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Singleton
    @Provides
    @Mock
    fun providesAuthApiMock(api: AuthApiMockImpl): AuthApi = api

    @Singleton
    @Provides
    @Origin
    fun providesLogoutApi(@Logout retrofit: Retrofit): LogoutApi = retrofit.create(LogoutApi::class.java)

    @Singleton
    @Provides
    @Mock
    fun providesLogoutApiMock(api: LogoutApiMockImpl): LogoutApi = api

    @Singleton
    @Provides
    @Origin
    fun providesUserApi(@Main retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Singleton
    @Provides
    @Mock
    fun providesUserApiMock(api: UserApiMockImpl): UserApi = api

    @Singleton
    @Provides
    @Origin
    fun providePostsApi(@Main retrofit: Retrofit): PostsApi = retrofit.create(PostsApi::class.java)

    @Singleton
    @Provides
    @Mock
    fun providePostsApiMock(api: PostsApiMockImpl): PostsApi = api

    @Singleton
    @Provides
    @Origin
    fun provideFriendsApi(@Main retrofit: Retrofit): FriendsApi = retrofit.create(FriendsApi::class.java)

    @Singleton
    @Provides
    @Mock
    fun provideFriendsApiMock(api: FriendsApiMockImpl): FriendsApi = api

    @Singleton
    @Provides
    fun provideSearchApi(@Main retrofit: Retrofit): SearchApi = retrofit.create(SearchApi::class.java)
}
