package com.redmadrobot.app.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentSearchBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class SearchFragment : BaseFragment(R.layout.fragment_search) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }
    private val binding by viewBinding<FragmentSearchBinding>()

    override fun showProgressLoader(show: Boolean, fm: FragmentManager) {
        binding.searchProgressIndicator.isVisible = show
    }

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = SearchEpoxyController(
            onPlusClicked = viewModel::onPlusClicked,
            onUpdateClicked = viewModel::onUpdateClicked
        )
        binding.searchRecyclerView.setController(controller)

        observe(viewModel.eventsQueue, ::onEvent)
        observe(viewModel.userList, controller::setData)
        observe(viewModel.isLoading) { isLoading ->
            showProgressLoader(isLoading)
            renderSearchRecyclerView(isLoading)
        }
        observe(viewModel.clearSearchText, ::renderSearchTextFieldContent)
        observe(viewModel.isSearchFieldEnabled, ::renderSearchFieldAvailability)

        binding.searchToolbar.setNavigationOnClickListener {
            viewModel.onToolbarBackClicked()
        }

        binding.searchEditText.doAfterTextChanged { text ->
            viewModel.onSearchUserTextChanged(userName = text.toString())
        }

        binding.searchTextLayout.setEndIconOnClickListener {
            viewModel.onSearchFieldCrossClicked()
        }
    }

    private fun renderSearchRecyclerView(isGone: Boolean) {
        binding.searchRecyclerView.isGone = isGone
    }

    private fun renderSearchTextFieldContent(clearText: Boolean) {
        if (clearText) {
            binding.searchEditText.text?.clear()
            viewModel.afterSearchFieldCleared()
        }
    }

    private fun renderSearchFieldAvailability(isEnabled: Boolean) {
        binding.searchTextLayout.isEnabled = isEnabled
    }
}
