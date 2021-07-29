package com.example.fluffrapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

class user(
    //info from registration
    var user_id: String = "",
    val ownerImage: String = "",
    val ownerFirstName: String = "",
    val ownerLastName: String = "",
    val email: String = "",

    val location: String = "",
    val ownerBio: String = "",


    //check if profile is completed
    val profileCompleted: Int = 0


) : Parcelable

