package com.redmadrobot.app.ui.googlemap

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.redmadrobot.app.model.MapMarker
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class GoogleMapViewModel @AssistedInject constructor(@Assisted private val marker: MapMarker?) : BaseViewModel() {

    private val mapViewState: MutableLiveData<MapViewState> = MutableLiveData(createInitialState())
    val mapMarker = mapViewState.mapDistinct { it.mapMarker }
    val defaultMapMarker = mapViewState.mapDistinct { it.deviceLocation }
    private var state: MapViewState by mapViewState

    init {
        setSelectedCoordinates()
    }

    private fun setSelectedCoordinates() {
        marker?.let { state = state.copy(mapMarker = it) }
    }

    fun onMapClicked(point: LatLng) {
        val mapMarker = MapMarker(coordinates = point, animatedCamera = true)
        state = state.copy(mapMarker = mapMarker)
    }

    fun getDeviceLocation(lastKnownLocation: Location?) {
        state = state.copy(deviceLocation = lastKnownLocation)
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted marker: MapMarker?): GoogleMapViewModel
    }

    private fun createInitialState(): MapViewState {
        return MapViewState(mapMarker = null, deviceLocation = null)
    }

    data class MapViewState(
        val mapMarker: MapMarker?,
        val deviceLocation: Location?,
    )
}
