package com.redmadrobot.app.ui.bottomnav

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentBottomNavBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.app.ui.base.viewmodel.BottomNavigationEvent
import com.redmadrobot.extensions.lifecycle.Event
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class BottomNavFragment : BaseFragment(R.layout.fragment_bottom_nav) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<BottomNavViewModel> { viewModelFactory }
    private val binding by viewBinding<FragmentBottomNavBinding>()

    // список для всех фрагментов, в которых отображается [BottomNavigationView]
    private val bottomNavVisibilityList = listOf(R.id.feed_fragment, R.id.profile_fragment)

    override fun onEvent(event: Event) {
        if (event is BottomNavigationEvent) {
            binding.bottomNavView.selectedItemId = event.menuGraphId
        } else {
            super.onEvent(event)
        }
    }

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.bottomNavigationEvents, ::onEvent)

        setupBottomNavBar()
    }

    private fun setupBottomNavBar() {
        val host = childFragmentManager.findFragmentById(R.id.bottom_nav_host_fragment) as NavHostFragment
        val navController = host.navController
        binding.bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isVisible = destination.id in bottomNavVisibilityList
            binding.bottomNavView.isVisible = isVisible
            binding.bottomNavView.isEnabled = isVisible
            viewModel.onDestinationChanged(destination.parent?.id)
        }
    }
}
