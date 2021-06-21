package com.redmadrobot.app.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.app.model.ProfileItem
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.NavigateUp
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.app.utils.Searcher
import com.redmadrobot.app.utils.converters.ErrorConverter
import com.redmadrobot.app.utils.converters.ProfileListMapper
import com.redmadrobot.data.repository.FriendsRepository
import com.redmadrobot.data.repository.SearchRepository
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val friendsRepository: FriendsRepository,
    private val searcher: Searcher,
    private val error: ErrorConverter,
    private val profileListMapper: ProfileListMapper,
) : BaseViewModel() {

    override fun onCleared() {
        searchRepository.clearCache()
    }

    private val searchViewState: MutableLiveData<SearchViewState> =
        MutableLiveData(createInitialState())
    val userList = searchViewState.mapDistinct { it.userList }
    val clearSearchText = searchViewState.mapDistinct { it.clearSearchText }
    val isLoading = searchViewState.mapDistinct { it.isLoading }
    val isSearchFieldEnabled = searchViewState.mapDistinct { it.isSearchFieldEnabled }
    private var state: SearchViewState by searchViewState

    init {
        searcher.observeField()
            .onEach { name -> searchFriends(name) }
            .launchIn(viewModelScope)

        searchRepository.searchResult
            .filterNotNull()
            .map { profileList -> profileListMapper.mapToUserList(profileList = profileList) }
            .onEach { userList -> state = state.copy(userList = userList) }
            .launchIn(viewModelScope)
    }

    private fun searchFriends(name: String) {
        searchRepository.searchFriends(name)
            .onStart { state = state.copy(isLoading = true) }
            .onCompletion { state = state.copy(isLoading = false) }
            .catch { throwable: Throwable ->
                state = state.copy(userList = profileListMapper.profileListError(), isSearchFieldEnabled = false)
                val message = error.message(throwable)
                eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
            }
            .launchIn(viewModelScope)
    }

    fun onToolbarBackClicked() {
        eventsQueue.offerEvent(NavigateUp)
    }

    fun onSearchUserTextChanged(userName: String) {
        state = state.copy(searchText = userName)
        searcher.setText(text = userName)
    }

    fun onPlusClicked(userId: String) {
        friendsRepository.addFriend(userId)
            .catch { throwable: Throwable ->
                state = state.copy(userList = profileListMapper.profileListError(), isSearchFieldEnabled = false)
                val message = error.message(throwable)
                eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
            }
            .launchIn(viewModelScope)
    }

    fun onSearchFieldCrossClicked() {
        state = state.copy(clearSearchText = true, userList = emptyList())
        searchRepository.clearCache()
        searcher.clearText()
    }

    fun afterSearchFieldCleared() {
        state = state.copy(clearSearchText = false)
    }

    fun onUpdateClicked() {
        state = state.copy(isSearchFieldEnabled = true)
        searchFriends(state.searchText)
    }

    private fun createInitialState(): SearchViewState {
        return SearchViewState(
            userList = emptyList(),
            searchText = "",
            clearSearchText = false,
            isSearchFieldEnabled = true,
            isLoading = false
        )
    }

    data class SearchViewState(
        val userList: List<ProfileItem>,
        val searchText: String,
        val clearSearchText: Boolean,
        val isSearchFieldEnabled: Boolean,
        val isLoading: Boolean,
    )
}
