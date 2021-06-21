package com.redmadrobot.app.ui.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.app.R
import com.redmadrobot.app.model.FeedItem
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.MainNavigateEvent
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.app.ui.bottomnav.BottomNavFragmentDirections
import com.redmadrobot.app.utils.converters.ErrorConverter
import com.redmadrobot.app.utils.converters.FeedListMapper
import com.redmadrobot.app.utils.providers.UserFromPostNavigator
import com.redmadrobot.data.repository.PostsRepository
import com.redmadrobot.domain.providers.DispatcherProvider
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FeedViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val feedListMapper: FeedListMapper,
    private val error: ErrorConverter,
    dispatcher: DispatcherProvider,
    userFromPostNavigator: UserFromPostNavigator,
) : BaseViewModel() {

    private val feedViewState: MutableLiveData<FeedViewState> = MutableLiveData(createInitialState())
    val feedList = feedViewState.mapDistinct { it.feedList }
    val isLoading = feedViewState.mapDistinct { it.isLoading }
    private var state: FeedViewState by feedViewState

    init {
        fetchPosts()
        userFromPostNavigator.invalidatePostList
            .onEach { fetchPosts() }
            .launchIn(viewModelScope)

        postsRepository.postList
            .map { postList ->
                if (postList.isNotEmpty()) {
                    feedListMapper.mapPostAndProfileListWithHeader(R.string.feed_header, postList)
                } else {
                    feedListMapper.mapNoFriends()
                }
            }
            .flowOn(dispatcher.default)
            .onEach { posts -> state = state.copy(feedList = posts) }
            .launchIn(viewModelScope)
    }

    private fun fetchPosts() {
        postsRepository.fetchPosts()
            .onStart { state = state.copy(isLoading = true) }
            .onCompletion { state = state.copy(isLoading = false) }
            .catch { throwable: Throwable ->
                state = state.copy(feedList = feedListMapper.postListError())
                val message = error.message(throwable)
                eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
            }
            .launchIn(viewModelScope)
    }

    fun onSearchFriendsClicked() {
        val action = BottomNavFragmentDirections.actionBottomNavFragmentToSearchFragment()
        eventsQueue.offerEvent(MainNavigateEvent(action))
    }

    fun onUpdateClicked() {
        fetchPosts()
    }

    fun onLikeClicked(id: String) {
        postsRepository.changeLike(id = id)
            .catch { throwable: Throwable ->
                val message = error.message(throwable)
                eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
            }.launchIn(viewModelScope)
    }

    private fun createInitialState(): FeedViewState {
        return FeedViewState(
            feedList = emptyList(),
            isLoading = false
        )
    }

    data class FeedViewState(
        val feedList: List<FeedItem>,
        val isLoading: Boolean,
    )
}
