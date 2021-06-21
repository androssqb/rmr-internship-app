package com.redmadrobot.app.ui.profile.friends

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentProfileFriendsBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class ProfileFriendsFragment : BaseFragment(R.layout.fragment_profile_friends) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<ProfileFriendsViewModel> { viewModelFactory }
    private val binding by viewBinding<FragmentProfileFriendsBinding>()

    override val anchorViewId: Int
        get() = R.id.bottom_nav_view

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = ProfileFriendsEpoxyController(
            onCrossClicked = viewModel::onCrossClicked,
            onSearchFriendsClicked = viewModel::onSearchFriendsClicked,
            onUpdateClicked = viewModel::onUpdateClicked
        )
        binding.profileFriendsRecyclerView.setController(controller)

        observe(viewModel.eventsQueue, ::onEvent)
        observe(viewModel.isLoading) { isLoading ->
            showProgressLoader(isLoading)
            renderProfileFriendsRecyclerView(isLoading)
        }
        observe(viewModel.friendList, controller::setData)
    }

    private fun renderProfileFriendsRecyclerView(isGone: Boolean) {
        binding.profileFriendsRecyclerView.isGone = isGone
    }
}
