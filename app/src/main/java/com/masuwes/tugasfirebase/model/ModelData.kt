package com.masuwes.tugasfirebase.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelData(
    var profile_image : String = "",
    var profile_name : String = "",
    var profile_class : String = "",
    var profile_address : String = ""
) : Parcelable