package com.redmadrobot.app.ui.base.fragment

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.redmadrobot.app.R
import com.redmadrobot.app.ui.MainActivity
import com.redmadrobot.app.ui.base.viewmodel.*
import com.redmadrobot.app.ui.dialog.ProgressLoaderDialog
import com.redmadrobot.extensions.lifecycle.Event

open class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected companion object {
        const val PROGRESS_LOADER = "progress_loader"
    }

    private val mainNavController by lazy {
        Navigation.findNavController(requireActivity() as MainActivity, R.id.main_nav_host_fragment)
    }

    protected open val anchorViewId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    protected open fun onEvent(event: Event) {
        when (event) {
            is ShowMessageEvent -> showMessage(event)
            is Navigate -> navigateTo(event.direction)
            is MainNavigateEvent -> mainNavigateTo(event.direction)
            is NavigateUp -> navigateUp()
            is PopBackStackEvent -> popBackStack()
        }
    }

    private fun showMessage(event: ShowMessageEvent) {
        val message = event.textMessage.ifEmpty { resources.getString(event.messageId) }
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
            anchorViewId?.let { setAnchorView(it) }
            show()
        }
    }

    protected open fun navigateTo(direction: NavDirections) {
        findNavController().navigate(direction)
    }

    protected open fun mainNavigateTo(direction: NavDirections) {
        mainNavController.navigate(direction)
    }

    protected open fun navigateUp() {
        findNavController().navigateUp()
    }

    protected open fun popBackStack() {
        findNavController().popBackStack()
    }

    protected open fun showProgressLoader(show: Boolean, fm: FragmentManager = childFragmentManager) {
        if (show) {
            if (fm.fragments.filterIsInstance<ProgressLoaderDialog>().isEmpty()) {
                ProgressLoaderDialog().show(fm, PROGRESS_LOADER)
            }
        } else {
            (fm.findFragmentByTag(PROGRESS_LOADER) as? DialogFragment)?.dismiss()
        }
    }

    protected open fun inject() {
        // переопределяем, где нужно для внедрения зависимостей
    }
}
