package com.example.fluffrapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

@Parcelize

class chat(

    var message_id: String = "",

    val sendUserid: String = "",
    val sendUserImage: String = "",
    val message: String = "",

    val receivedUserid: String = "",
    val receivedUserImage: String = "",
    val timestamp: Long = 0

) : Parcelable

