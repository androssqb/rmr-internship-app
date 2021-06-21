package com.redmadrobot.app.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redmadrobot.app.di.factory.ViewModelFactory
import com.redmadrobot.app.di.key.ViewModelKey
import com.redmadrobot.app.ui.MainActivityViewModel
import com.redmadrobot.app.ui.bottomnav.BottomNavViewModel
import com.redmadrobot.app.ui.done.DoneViewModel
import com.redmadrobot.app.ui.feed.FeedViewModel
import com.redmadrobot.app.ui.login.LoginViewModel
import com.redmadrobot.app.ui.post.PostViewModel
import com.redmadrobot.app.ui.profile.ProfileViewModel
import com.redmadrobot.app.ui.profile.edit.EditProfileViewModel
import com.redmadrobot.app.ui.profile.friends.ProfileFriendsViewModel
import com.redmadrobot.app.ui.profile.likes.ProfileLikesViewModel
import com.redmadrobot.app.ui.profile.posts.ProfilePostsViewModel
import com.redmadrobot.app.ui.search.SearchViewModel
import com.redmadrobot.app.ui.signin.SignInViewModel
import com.redmadrobot.app.ui.signupfirst.SignUpFirstViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("TooManyFunctions")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory):
        ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun provideMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun provideSignInViewModel(viewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpFirstViewModel::class)
    abstract fun provideSignUpFirstViewModel(viewModel: SignUpFirstViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DoneViewModel::class)
    abstract fun provideDoneViewModel(viewModel: DoneViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun provideFeedViewModel(viewModel: FeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    abstract fun providePostViewModel(viewModel: PostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun provideProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun provideEditProfileViewModel(viewModel: EditProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfilePostsViewModel::class)
    abstract fun provideProfilePostsViewModel(viewModel: ProfilePostsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileLikesViewModel::class)
    abstract fun provideProfileLikesViewModel(viewModel: ProfileLikesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileFriendsViewModel::class)
    abstract fun provideProfileFriendsViewModel(viewModel: ProfileFriendsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BottomNavViewModel::class)
    abstract fun provideBottomNavViewModel(viewModel: BottomNavViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun provideSearchViewModel(viewModel: SearchViewModel): ViewModel
}
