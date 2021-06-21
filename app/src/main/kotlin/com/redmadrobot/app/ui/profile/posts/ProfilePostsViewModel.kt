package com.redmadrobot.app.ui.profile.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.app.R
import com.redmadrobot.app.model.FeedItem
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.app.utils.converters.ErrorConverter
import com.redmadrobot.app.utils.converters.FeedListMapper
import com.redmadrobot.data.repository.PostsRepository
import com.redmadrobot.domain.di.BottomNavigationEvents
import com.redmadrobot.domain.providers.DispatcherProvider
import com.redmadrobot.extensions.lifecycle.EventQueue
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ProfilePostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val feedListMapper: FeedListMapper,
    private val error: ErrorConverter,
    @BottomNavigationEvents val bottomNavigationEvents: EventQueue,
    dispatcher: DispatcherProvider,
) : BaseViewModel() {

    private val profilePostsViewState: MutableLiveData<ProfilePostsViewState> = MutableLiveData(createInitialState())
    val userPostList = profilePostsViewState.mapDistinct { it.userPostList }
    val isLoading = profilePostsViewState.mapDistinct { it.isLoading }
    private var state: ProfilePostsViewState by profilePostsViewState

    init {
        fetchUserPosts()

        postsRepository.userPosts
            .map { postList ->
                if (postList.isNotEmpty()) {
                    feedListMapper.mapPostListMapMemory(postList)
                } else {
                    feedListMapper.mapNoUserPosts()
                }
            }
            .flowOn(dispatcher.default)
            .onEach { posts -> state = state.copy(userPostList = posts) }
            .launchIn(viewModelScope)
    }

    private fun fetchUserPosts() {
        postsRepository.fetchUsersPostsFromServer()
            .onStart { state = state.copy(isLoading = true) }
            .onCompletion { state = state.copy(isLoading = false) }
            .catch { throwable: Throwable ->
                state = state.copy(userPostList = feedListMapper.postListError())
                val message = error.message(throwable)
                eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
            }
            .launchIn(viewModelScope)
    }

    fun onCreatePostClicked() {
        bottomNavigationEvents.navigate(R.id.post_graph)
    }

    fun onUpdateClicked() {
        fetchUserPosts()
    }

    private fun createInitialState(): ProfilePostsViewState {
        return ProfilePostsViewState(
            userPostList = emptyList(),
            isLoading = false
        )
    }

    data class ProfilePostsViewState(
        val userPostList: List<FeedItem>,
        val isLoading: Boolean,
    )
}
