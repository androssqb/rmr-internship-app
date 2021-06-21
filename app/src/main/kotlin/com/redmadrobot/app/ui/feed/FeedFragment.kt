package com.redmadrobot.app.ui.feed

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentFeedBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class FeedFragment : BaseFragment(R.layout.fragment_feed) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<FeedViewModel> { viewModelFactory }
    private val binding by viewBinding<FragmentFeedBinding>()

    override val anchorViewId: Int
        get() = R.id.bottom_nav_view

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = FeedEpoxyController(
            onLikeClicked = viewModel::onLikeClicked,
            onSearchClicked = viewModel::onSearchFriendsClicked,
            onUpdateClicked = viewModel::onUpdateClicked
        )
        binding.feedRecyclerView.setController(controller)

        observe(viewModel.eventsQueue, ::onEvent)
        observe(viewModel.isLoading) { isLoading ->
            showProgressLoader(isLoading)
            renderFeedRecyclerView(isLoading)
        }
        observe(viewModel.feedList, controller::setData)
    }

    private fun renderFeedRecyclerView(isGone: Boolean) {
        binding.feedRecyclerView.isGone = isGone
    }
}
