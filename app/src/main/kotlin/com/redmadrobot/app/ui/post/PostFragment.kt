package com.redmadrobot.app.ui.post

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import coil.clear
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.google.android.gms.maps.model.LatLng
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentPostBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.app.ui.googlemap.GoogleMapFragment
import com.redmadrobot.extensions.lifecycle.Event
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import javax.inject.Inject

class PostFragment : BaseFragment(R.layout.fragment_post) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<PostViewModel> { viewModelFactory }
    private val binding by viewBinding<FragmentPostBinding>()

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            viewModel.updateCameraAndStoragePermissionsState()
        }

    private val capturePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
        viewModel.isPhotoCaptureSuccessful(isSuccessful)
    }

    private val requestMapPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            viewModel.updateLocationFinePermissionState(granted)
        }

    override fun onEvent(event: Event) {
        when (event) {
            is PermissionsRequestEvent -> requestCameraPermission.launch(event.permissions)
            is PhotoCaptureEvent -> capturePhoto.launch(event.uri)
            is OnePermissionRequestEvent -> requestMapPermission.launch(event.permission)
            else -> super.onEvent(event)
        }
    }

    override val anchorViewId: Int
        get() = R.id.post_creation_set_image_button

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(GoogleMapFragment.REQUEST_KEY) { _, bundle ->
            val result = bundle.get(GoogleMapFragment.LAT_LNG) as LatLng
            viewModel.getGeoResult(result)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openKeyboard()
        interceptOnBackPressed()

        observe(viewModel.photoPreviewUri, ::renderImagePreview)
        observe(viewModel.eventsQueue, ::onEvent)
        observe(viewModel.isLoading, ::showProgressLoader)
        observe(viewModel.clearText, ::renderPostText)
        observe(viewModel.geolocationAddress, ::renderGeolocation)

        binding.postCreationToolbar.setNavigationOnClickListener {
            viewModel.onNavigationActionPressed()
        }

        binding.postCreationSetImageButton.setOnClickListener {
            viewModel.onImageClicked()
        }

        binding.postCreationCreatePostButton.setOnClickListener {
            viewModel.onArrowClicked(postText = binding.postCreationEditText.text.toString())
        }

        binding.postCreationPhotoRemoveButton.setOnClickListener {
            viewModel.onCrossClicked()
        }

        binding.postCreationSetGeoButton.setOnClickListener {
            viewModel.onGeoClicked()
        }

        binding.postCreationGeoButton.setOnClickListener {
            viewModel.onGeoClicked()
        }
    }

    private fun interceptOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onNavigationActionPressed()
        }
    }

    private fun openKeyboard() {
        if (binding.postCreationEditText.requestFocus()) {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.postCreationEditText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun renderPostText(clearText: Boolean) {
        if (clearText) {
            binding.postCreationEditText.text?.clear()
        }
    }

    private fun renderImagePreview(imageUri: Uri) {
        binding.postCreationPhotoRemoveButton.isVisible = imageUri != Uri.EMPTY
        if (imageUri != Uri.EMPTY) {
            val imageCornerRadius = resources.getDimension(R.dimen.coil_image_corner_radius_12_dp)
            binding.postCreationPhotoPreview.load(imageUri) {
                placeholder(R.drawable.post_photo_background)
                transformations(RoundedCornersTransformation(imageCornerRadius))
                scale(Scale.FILL)
            }
        } else {
            binding.postCreationPhotoPreview.clear()
        }
    }

    private fun renderGeolocation(address: String?) {
        binding.postCreationGeoButton.isVisible = address != null
        binding.postCreationGeoButton.text = address
    }
}
