package com.redmadrobot.app.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentProfileBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<ProfileViewModel> { viewModelFactory }
    private val binding by viewBinding<FragmentProfileBinding>()

    override val anchorViewId: Int
        get() = R.id.bottom_nav_view

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.eventsQueue, ::onEvent)
        observe(viewModel.profileViewState, ::renderUserData)
        observe(viewModel.isLoading, ::showProgressLoader)

        binding.profilePatchUserDataButton.setOnClickListener {
            viewModel.onPatchUserClicked()
        }

        binding.profileLogoutButton.setOnClickListener {
            viewModel.onLogoutClicked()
        }

        binding.profileViewPager.apply {
            adapter = ProfileViewPagerAdapter(
                fragmentManager = childFragmentManager,
                lifecycle = lifecycle
            )
        }
        TabLayoutMediator(binding.profileTabLayout, binding.profileViewPager) { tab, position ->
            tab.text = resources.getString(ProfileViewPagerAdapter.getTabText(position = position))
        }.attach()
    }

    private fun renderUserData(userData: ProfileViewModel.ProfileViewState) {
        binding.profileNicknameDescription.text = userData.nickname
        binding.profileUserFullName.text = userData.fullName
        binding.profileUserAge.text = userData.age
    }
}
