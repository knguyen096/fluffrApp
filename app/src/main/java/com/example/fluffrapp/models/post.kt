package com.example.fluffrapp.models

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class post(
    val user_id: String = "",
    val user_name: String = "",
    val location: String = "",
    val content: String = "",
    val postImage: String = "",
    val ownerImage: String = "",

    var timestamp: Long = 0,
    var post_id: String = ""

) : Parcelable