package com.example.fluffrapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

class pet(

    var user_id: String = "",
    val petImage: String = "",
    val petName: String = "",
    val petBio: String = "",
    val petGender:String = "",
    val petAge: String = "",

    val likes: String = "",
    val dislikes: String = "",
    var pet_id:String = ""



) : Parcelable

