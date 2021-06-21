package com.redmadrobot.app.ui.googlemap

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.redmadrobot.app.R
import com.redmadrobot.app.databinding.FragmentGoogleMapBinding
import com.redmadrobot.app.di.DI
import com.redmadrobot.app.model.MapMarker
import com.redmadrobot.app.ui.base.fragment.BaseFragment
import com.redmadrobot.app.utils.extension.assistedViewModel
import com.redmadrobot.extensions.lifecycle.observe
import com.redmadrobot.extensions.viewbinding.viewBinding
import timber.log.Timber
import javax.inject.Inject

class GoogleMapFragment : BaseFragment(R.layout.fragment_google_map), OnMapReadyCallback {

    companion object {
        const val REQUEST_KEY = "google_map_point"
        const val LAT_LNG = "latLng"
        private const val DEFAULT_ZOOM = 15f
    }

    @Inject
    lateinit var factory: GoogleMapViewModel.Factory
    private val viewModel by assistedViewModel { factory.create(marker = args.mapMarker) }
    private val args by navArgs<GoogleMapFragmentArgs>()
    private val binding by viewBinding<FragmentGoogleMapBinding>()

    private var googleMap: GoogleMap? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun inject() {
        DI.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.googleMap.onCreate(savedInstanceState)
        binding.googleMap.onResume()
        binding.googleMap.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        enableMyLocation()
        googleMap?.setOnMapClickListener(viewModel::onMapClicked)

        observe(viewModel.mapMarker, ::renderMapMarker)
        observe(viewModel.defaultMapMarker, ::renderDefaultMapMarker)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        googleMap = null
        fusedLocationProviderClient = null
    }

    private fun renderMapMarker(marker: MapMarker?) {
        if (marker != null) {
            val point = marker.coordinates
            val markerOptions = MarkerOptions()
            markerOptions.position(point)
            googleMap?.clear()
            if (marker.animatedCamera) {
                googleMap?.animateCamera(CameraUpdateFactory.newLatLng(point))
            } else {
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(point, DEFAULT_ZOOM))
            }
            googleMap?.addMarker(markerOptions)
            setFragmentResult(REQUEST_KEY, bundleOf(LAT_LNG to point))
        } else {
            getDeviceLocation()
        }
    }

    private fun renderDefaultMapMarker(defaultLocation: Location?) {
        defaultLocation?.let {
            val point = LatLng(it.latitude, it.longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(point)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(point, DEFAULT_ZOOM))
            googleMap?.addMarker(markerOptions)
            setFragmentResult(REQUEST_KEY, bundleOf(LAT_LNG to point))
        }
    }

    private fun enableMyLocation() {
        googleMap?.isMyLocationEnabled = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getDeviceLocation() {
        try {
            val locationResult = fusedLocationProviderClient?.lastLocation
            locationResult?.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    viewModel.getDeviceLocation(task.result)
                }
            }
        } catch (exception: SecurityException) {
            Timber.e(exception)
        }
    }
}
