package com.redmadrobot.app.ui.profile.posts

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentProfilePostsBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class ProfilePostsFragment : BaseFragment(R.layout.fragment_profile_posts) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<ProfilePostsViewModel> { viewModelFactory }
    private val binding by viewBinding<FragmentProfilePostsBinding>()

    override val anchorViewId: Int
        get() = R.id.bottom_nav_view

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = ProfilePostsEpoxyController(
            onCreatePostsClicked = viewModel::onCreatePostClicked,
            onUpdateClicked = viewModel::onUpdateClicked
        )
        binding.profilePostsRecyclerView.setController(controller)

        observe(viewModel.eventsQueue, ::onEvent)
        observe(viewModel.isLoading) { isLoading ->
            showProgressLoader(isLoading, parentFragmentManager)
            renderProfilePostsRecyclerView(isLoading)
        }
        observe(viewModel.userPostList, controller::setData)
    }

    private fun renderProfilePostsRecyclerView(isGone: Boolean) {
        binding.profilePostsRecyclerView.isGone = isGone
    }
}
