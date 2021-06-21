package com.redmadrobot.app.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MapMarker(
    val coordinates: LatLng,
    val animatedCamera: Boolean
) : Parcelable
