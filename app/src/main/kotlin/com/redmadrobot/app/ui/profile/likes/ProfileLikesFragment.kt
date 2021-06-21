package com.redmadrobot.app.ui.profile.likes

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentProfileLikesBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class ProfileLikesFragment : BaseFragment(R.layout.fragment_profile_likes) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<ProfileLikesViewModel> { viewModelFactory }
    private val binding by viewBinding<FragmentProfileLikesBinding>()

    override val anchorViewId: Int
        get() = R.id.bottom_nav_view

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = ProfileLikesEpoxyController(
            onGoToFeedClicked = viewModel::onGoToFeedClicked,
            onUpdateClicked = viewModel::onUpdateClicked,
            onLikeClicked = viewModel::onLikeClicked
        )
        binding.profileLikesRecyclerView.setController(controller)

        observe(viewModel.eventsQueue, ::onEvent)
        observe(viewModel.isLoading) { isLoading ->
            showProgressLoader(isLoading)
            renderProfileLikesRecyclerView(isLoading)
        }
        observe(viewModel.favoriteList, controller::setData)
    }

    private fun renderProfileLikesRecyclerView(isGone: Boolean) {
        binding.profileLikesRecyclerView.isGone = isGone
    }
}
