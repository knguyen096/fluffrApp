package com.example.fluffrapp.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object constants {

    const val USERS: String = "users"
    const val FLUFPREFERENCES: String = "flufPreferences"
    const val LOGGED_IN_USERNAME: String = "logging_in_username"
    const val MESSAGES: String = "messages"

    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val EXTRA_USER_ID:String = "extra_user_id"
    const val EXTRA_USER_POST_ID: String = "extra_user_post_id"
    const val EXTRA_USER_IMAGE: String = "extra_user_image"

    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQ_CODE = 1

    const val USER_PROF_IMAGE: String = "ownerImage"
    const val LAST_NAME: String = "ownerLastName"
    const val FIRST_NAME: String = "ownerFirstName"
    const val USER_BIO: String = "ownerBio"

    const val PET_PROF_IMAGE:String = "petImage"
    const val EXTRA_PET_ID: String = "extra_pet_id"
    const val PET_NAME: String = "petName"
    const val LOCATION: String = "location"
    const val PET_BIO: String = "petBio"
    const val PET: String = "pets"

    const val MALE: String = "Male"
    const val FEMALE: String = "Female"
    const val PET_GENDER: String = "petGender"

    const val COMPLETE_PROFILE: String = "profileCompleted"

    const val POST_IMAGE:String = "postImage"
    const val POST: String = "posts"


    const val USER_ID: String = "user_id"

    fun showImagChosen(activity: Activity) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQ_CODE)
    }

    fun getFileExt(activity: Activity, uri: Uri?):String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}