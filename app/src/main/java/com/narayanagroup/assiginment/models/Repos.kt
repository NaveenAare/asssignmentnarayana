package com.narayanagroup.assiginment.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Repos(
    @SerializedName("items")
    val playersList: List<items>,
    //val meta: Meta
) : Parcelable