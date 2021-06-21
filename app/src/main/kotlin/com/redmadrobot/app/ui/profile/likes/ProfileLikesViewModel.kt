package com.redmadrobot.app.ui.profile.likes

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

class ProfileLikesViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val feedListMapper: FeedListMapper,
    private val error: ErrorConverter,
    @BottomNavigationEvents val bottomNavigationEvents: EventQueue,
    dispatcher: DispatcherProvider,
) : BaseViewModel() {

    private val profileLikesViewState: MutableLiveData<ProfileLikesViewState> = MutableLiveData(createInitialState())
    val favoriteList = profileLikesViewState.mapDistinct { it.favoriteList }
    val isLoading = profileLikesViewState.mapDistinct { it.isLoading }
    private var state: ProfileLikesViewState by profileLikesViewState

    init {
        fetchFavoritePosts()

        postsRepository.favoriteList
            .map { postList ->
                if (postList.isNotEmpty()) {
                    feedListMapper.mapPostAndProfileList(postList)
                } else {
                    feedListMapper.mapNoUserLikes()
                }
            }
            .flowOn(dispatcher.default)
            .onEach { posts -> state = state.copy(favoriteList = posts) }
            .launchIn(viewModelScope)
    }

    private fun fetchFavoritePosts() {
        postsRepository.fetchFavoritePosts()
            .onStart { state = state.copy(isLoading = true) }
            .onCompletion { state = state.copy(isLoading = false) }
            .catch { throwable: Throwable ->
                state = state.copy(favoriteList = feedListMapper.postListError())
                val message = error.message(throwable)
                eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
            }
            .launchIn(viewModelScope)
    }

    fun onGoToFeedClicked() {
        bottomNavigationEvents.navigate(R.id.feed_graph)
    }

    fun onUpdateClicked() {
        fetchFavoritePosts()
    }

    fun onLikeClicked(id: String) {
        postsRepository.changeLike(id = id)
            .catch { throwable: Throwable ->
                val message = error.message(throwable)
                eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
            }.launchIn(viewModelScope)
    }

    private fun createInitialState(): ProfileLikesViewState {
        return ProfileLikesViewState(
            favoriteList = emptyList(),
            isLoading = false
        )
    }

    data class ProfileLikesViewState(
        val favoriteList: List<FeedItem>,
        val isLoading: Boolean,
    )
}
