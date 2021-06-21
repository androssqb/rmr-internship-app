package com.redmadrobot.app.ui.profile.friends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.app.model.FriendItem
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.MainNavigateEvent
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.app.ui.bottomnav.BottomNavFragmentDirections
import com.redmadrobot.app.utils.converters.ErrorConverter
import com.redmadrobot.app.utils.converters.FriendListMapper
import com.redmadrobot.data.repository.FriendsRepository
import com.redmadrobot.domain.providers.DispatcherProvider
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ProfileFriendsViewModel @Inject constructor(
    private val friendsRepository: FriendsRepository,
    private val friendListMapper: FriendListMapper,
    private val error: ErrorConverter,
    dispatcher: DispatcherProvider,
) : BaseViewModel() {

    private val profileFriendsViewState: MutableLiveData<ProfileFriendsViewState> =
        MutableLiveData(createInitialState())
    val friendList = profileFriendsViewState.mapDistinct { it.friendList }
    val isLoading = profileFriendsViewState.mapDistinct { it.isLoading }
    private var state: ProfileFriendsViewState by profileFriendsViewState

    init {
        fetchFriendList()

        friendsRepository.friendList
            .map { friendList ->
                if (friendList.isNotEmpty()) {
                    friendListMapper.mapToFriendListWithFooter(friendList)
                } else {
                    friendListMapper.mapNoFriends()
                }
            }
            .flowOn(dispatcher.default)
            .onEach { friends -> state = state.copy(friendList = friends) }
            .launchIn(viewModelScope)
    }

    private fun fetchFriendList() {
        friendsRepository.fetchUserFriendList()
            .onStart { state = state.copy(isLoading = true) }
            .onCompletion { state = state.copy(isLoading = false) }
            .catch { throwable: Throwable ->
                state = state.copy(friendList = friendListMapper.friendListError())
                val message = error.message(throwable)
                eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
            }
            .launchIn(viewModelScope)
    }

    fun onCrossClicked(friendId: String) {
        friendsRepository.deleteFriend(friendId)
            .catch { throwable: Throwable ->
                state = state.copy(friendList = friendListMapper.friendListError())
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
        fetchFriendList()
    }

    private fun createInitialState(): ProfileFriendsViewState {
        return ProfileFriendsViewState(
            friendList = emptyList(),
            isLoading = false
        )
    }

    data class ProfileFriendsViewState(
        val friendList: List<FriendItem>,
        val isLoading: Boolean,
    )
}
