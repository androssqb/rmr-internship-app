package com.redmadrobot.app.ui.post

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.redmadrobot.app.R
import com.redmadrobot.app.model.MapMarker
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.Navigate
import com.redmadrobot.app.ui.base.viewmodel.NavigateUp
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.app.utils.converters.ErrorConverter
import com.redmadrobot.app.utils.converters.PostDataConverter
import com.redmadrobot.app.utils.converters.PostRequestDataConverter
import com.redmadrobot.app.utils.providers.PermissionCheckImpl
import com.redmadrobot.app.utils.providers.UserFromPostNavigator
import com.redmadrobot.data.repository.PostsRepository
import com.redmadrobot.domain.di.BottomNavigationEvents
import com.redmadrobot.domain.providers.PermissionCheck
import com.redmadrobot.domain.providers.PhotoFileGenerator
import com.redmadrobot.extensions.lifecycle.EventQueue
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@Suppress("LongParameterList", "TooManyFunctions")
class PostViewModel @Inject constructor(
    private val userFromPostNavigator: UserFromPostNavigator,
    @BottomNavigationEvents val bottomNavigationEvents: EventQueue,
    private val postsRepository: PostsRepository,
    private val error: ErrorConverter,
    private val photoFileGenerator: PhotoFileGenerator,
    private val permissionCheck: PermissionCheck,
    private val postRequestDataConverter: PostRequestDataConverter,
    private val postDataConverter: PostDataConverter,
) : BaseViewModel() {

    private val postViewState: MutableLiveData<PostViewState> = MutableLiveData(createInitialState())
    val photoPreviewUri = postViewState.mapDistinct { it.photoPreviewUri }
    val clearText = postViewState.mapDistinct { it.clearTextOnNavigation }
    val geolocationAddress = postViewState.mapDistinct {
        postDataConverter
            .convertCoordinatesToAddressPreview(
                lat = state.coordinates?.latitude,
                lon = state.coordinates?.longitude
            )
    }
    val isLoading = postViewState.mapDistinct { it.isLoading }
    private var state: PostViewState by postViewState

    fun onArrowClicked(postText: String) {
        if (postText.isNotEmpty()) {
            val image = postRequestDataConverter.convertFileDataToMultipartBody(state.photoPreviewUri)
            val text = postRequestDataConverter.convertPostTextToRequestBody(postText)
            postsRepository.createPost(
                postText = text,
                image = image,
                longitude = state.coordinates?.longitude,
                latitude = state.coordinates?.latitude
            )
                .onStart { state = state.copy(isLoading = true) }
                .onCompletion { state = state.copy(isLoading = false) }
                .onEach {
                    eventsQueue.offerEvent(NavigateUp)
                    state = state.copy(clearTextOnNavigation = true)
                    state = createInitialState()
                }
                .catch { throwable: Throwable ->
                    val message = error.message(throwable)
                    eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
                }.launchIn(viewModelScope)
        }
    }

    fun onImageClicked() {
        if (permissionCheck.cameraAndStoragePermissionsGranted()) {
            capturePhoto()
        } else {
            eventsQueue.offerEvent(PermissionsRequestEvent(PermissionCheckImpl.cameraAndStoragePermissions))
        }
    }

    fun updateCameraAndStoragePermissionsState() {
        if (permissionCheck.cameraAndStoragePermissionsGranted()) {
            capturePhoto()
        } else {
            eventsQueue.offerEvent(ShowMessageEvent(R.string.photo_permission_denied_error))
        }
    }

    private fun capturePhoto() {
        val uri = photoFileGenerator.getUriFromGeneratedFile()
        if (uri != null) {
            state = state.copy(photoFileUri = uri)
            eventsQueue.offerEvent(PhotoCaptureEvent(uri))
        } else {
            eventsQueue.offerEvent(ShowMessageEvent(R.string.photo_file_creation_error))
        }
    }

    fun isPhotoCaptureSuccessful(isSuccessful: Boolean) {
        state = if (isSuccessful) {
            state.copy(photoPreviewUri = state.photoFileUri, photoFileUri = Uri.EMPTY)
        } else {
            state.copy(photoFileUri = Uri.EMPTY)
        }
    }

    fun onCrossClicked() {
        state = state.copy(photoPreviewUri = Uri.EMPTY)
    }

    fun onNavigationActionPressed() {
        bottomNavigationEvents.navigate(userFromPostNavigator.previousGraphId)
        state = state.copy(clearTextOnNavigation = true)
        state = createInitialState()
    }

    fun onGeoClicked() {
        if (permissionCheck.locationFinePermissionsGranted()) {
            val mapMarker = state.coordinates?.let {
                MapMarker(
                    coordinates = LatLng(it.latitude, it.longitude),
                    animatedCamera = false
                )
            }
            val action = PostFragmentDirections.actionPostFragmentToGoogleMapFragment(mapMarker)
            eventsQueue.offerEvent(Navigate(action))
        } else {
            eventsQueue.offerEvent(OnePermissionRequestEvent(PermissionCheckImpl.googleMapPermission))
        }
    }

    fun onGeolocationClicked() {
        state = state.copy(coordinates = null)
    }

    fun getGeoResult(coordinates: LatLng) {
        state = state.copy(coordinates = coordinates)
    }

    fun updateLocationFinePermissionState(granted: Boolean) {
        if (granted) {
            val action = PostFragmentDirections.actionPostFragmentToGoogleMapFragment(null)
            eventsQueue.offerEvent(Navigate(action))
        } else {
            eventsQueue.offerEvent(ShowMessageEvent(R.string.location_permission_denied_error))
        }
    }

    private fun createInitialState(): PostViewState {
        return PostViewState(
            photoPreviewUri = Uri.EMPTY,
            photoFileUri = Uri.EMPTY,
            clearTextOnNavigation = false,
            coordinates = null,
            isLoading = false
        )
    }

    data class PostViewState(
        val photoPreviewUri: Uri,
        val photoFileUri: Uri,
        val clearTextOnNavigation: Boolean,
        val coordinates: LatLng?,
        val isLoading: Boolean,
    )
}
