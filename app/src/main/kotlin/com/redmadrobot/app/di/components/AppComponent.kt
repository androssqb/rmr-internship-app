package com.redmadrobot.app.di.components

import android.content.Context
import com.redmadrobot.app.di.modules.*
import com.redmadrobot.app.ui.MainActivity
import com.redmadrobot.app.ui.bottomnav.BottomNavFragment
import com.redmadrobot.app.ui.done.DoneFragment
import com.redmadrobot.app.ui.feed.FeedFragment
import com.redmadrobot.app.ui.googlemap.GoogleMapFragment
import com.redmadrobot.app.ui.login.LoginFragment
import com.redmadrobot.app.ui.post.PostFragment
import com.redmadrobot.app.ui.profile.ProfileFragment
import com.redmadrobot.app.ui.profile.edit.EditProfileFragment
import com.redmadrobot.app.ui.profile.friends.ProfileFriendsFragment
import com.redmadrobot.app.ui.profile.likes.ProfileLikesFragment
import com.redmadrobot.app.ui.profile.posts.ProfilePostsFragment
import com.redmadrobot.app.ui.search.SearchFragment
import com.redmadrobot.app.ui.signin.SignInFragment
import com.redmadrobot.app.ui.signupfirst.SignUpFirstFragment
import com.redmadrobot.app.ui.signupsecond.SignUpSecondFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Suppress("TooManyFunctions")
@Singleton
@Component(
    modules = [
        ViewModelModule::class,
        DataModule::class,
        NetworkModule::class,
        UseCaseModule::class,
        ApiModule::class,
        DispatcherModule::class,
        SecurityModule::class,
        DatabaseModule::class,
        NavigationModule::class
    ]
)
interface AppComponent {

    // Activity
    fun inject(activity: MainActivity)

    // Fragments
    fun inject(fragment: LoginFragment)
    fun inject(fragment: SignUpFirstFragment)
    fun inject(fragment: SignUpSecondFragment)
    fun inject(fragment: SignInFragment)
    fun inject(fragment: DoneFragment)
    fun inject(fragment: FeedFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: ProfilePostsFragment)
    fun inject(fragment: ProfileLikesFragment)
    fun inject(fragment: ProfileFriendsFragment)
    fun inject(fragment: EditProfileFragment)
    fun inject(fragment: PostFragment)
    fun inject(fragment: BottomNavFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: GoogleMapFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}
