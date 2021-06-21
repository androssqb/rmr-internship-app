package com.redmadrobot.app.utils.converters

import android.location.Geocoder
import com.redmadrobot.app.R
import com.redmadrobot.domain.providers.ResourceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostDataConverter @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val geocoder: Geocoder,
) {

    private companion object {
        const val MIN_LAT: Double = -90.0
        const val MAX_LAT: Double = 90.0
        const val MIN_LON: Double = -180.0
        const val MAX_LON: Double = 180.0
    }

    @Suppress("ComplexCondition")
    fun convertCoordinatesToAddress(lat: Double?, lon: Double?): String {
        return if (
            lat != null &&
            lon != null &&
            lat in MIN_LAT..MAX_LAT &&
            lon in MIN_LON..MAX_LON
        ) {
            val address = geocoder.getFromLocation(lat, lon, 1).firstOrNull()
            address?.let {
                val country = it.countryName.orEmpty()
                val area = it.adminArea.orEmpty()
                when {
                    country.isNotEmpty() && area.isNotEmpty() -> "$country, $area"
                    country.isNotEmpty() -> country
                    else -> null
                }
            } ?: resourceProvider.getString(R.string.unknown_area)
        } else {
            resourceProvider.getString(R.string.unknown_area)
        }
    }

    fun convertCoordinatesToAddressPreview(lat: Double?, lon: Double?): String? {
        return if (lat != null && lon != null) {
            val address = geocoder.getFromLocation(lat, lon, 1).firstOrNull()
            address?.let {
                val country = it.countryName.orEmpty()
                val area = it.adminArea.orEmpty()
                when {
                    country.isNotEmpty() && area.isNotEmpty() -> "$country, $area"
                    country.isNotEmpty() -> country
                    else -> null
                }
            }
        } else {
            null
        }
    }
}
