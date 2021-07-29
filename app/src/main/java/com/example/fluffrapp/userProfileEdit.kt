package com.example.fluffrapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fluffrapp.base.baseActivity
import com.example.fluffrapp.utils.constants
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.example.fluffrapp.firestore.firestore
import com.example.fluffrapp.models.user
import com.example.fluffrapp.utils.glideLoader
import kotlinx.android.synthetic.main.activity_user_profile_edit.*
import kotlinx.android.synthetic.main.post_list_layout.*


import java.io.IOException

class userProfileEdit : baseActivity(), View.OnClickListener {

    //private lateinit var mUserDetails: user
    private var mSelectedImgFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Edit Owner Profile"
        setContentView(R.layout.activity_user_profile_edit)

        var mUserDetails = user()
        if (intent.hasExtra(constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(constants.EXTRA_USER_DETAILS)!!
        }


        et_first_name.setText(mUserDetails.ownerFirstName)
        et_last_name.setText(mUserDetails.ownerLastName)

        if (mUserDetails.profileCompleted == 0) {
            et_first_name.isEnabled = true
            et_last_name.isEnabled = true
            et_location.isEnabled = true
            et_bio.isEnabled = true

        } else {
            glideLoader(this@userProfileEdit).loadUserPicture(mUserDetails.ownerImage, iv_userPhoto)
            et_first_name.isEnabled = true
            et_first_name.setText(mUserDetails.ownerFirstName)
            et_last_name.isEnabled = true
            et_last_name.setText(mUserDetails.ownerLastName)
            et_location.isEnabled = true
            et_location.setText(mUserDetails.location)
            et_bio.isEnabled = true
            et_bio.setText(mUserDetails.ownerBio)
        }

        tv_photoedit.setOnClickListener(this@userProfileEdit)
        btn_save.setOnClickListener(this@userProfileEdit)


    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.tv_photoedit -> {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        constants.showImagChosen(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_save -> {
                    if (validateUserDetails()) {
                        if (mSelectedImgFileUri != null)
                            firestore().uploadImageToCloud(
                                this,
                                mSelectedImgFileUri,
                                constants.USER_PROF_IMAGE
                            )
                        else {
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetails() {
        var mUserDetails = user()
        val userHashMap = HashMap<String, Any>()

        val firstName = et_first_name.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.ownerFirstName) {
            userHashMap[constants.FIRST_NAME] = firstName
        }

        val lastName = et_last_name.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.ownerLastName) {
            userHashMap[constants.LAST_NAME] = lastName
        }

        val location = et_location.text.toString().trim { it <= ' ' }
        if (location != mUserDetails.location) {
            userHashMap[constants.LOCATION] = location
        }

        val bio = et_bio.text.toString().trim { it <= ' ' }
        if (bio != mUserDetails.ownerBio) {
            userHashMap[constants.USER_BIO] = bio
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[constants.USER_PROF_IMAGE] = mUserProfileImageURL
        }


        userHashMap[constants.COMPLETE_PROFILE] = 1

        firestore().updateUserProfile(this, userHashMap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                constants.showImagChosen(this)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == constants.PICK_IMAGE_REQ_CODE) {
                if (data != null) {
                    try {
                        mSelectedImgFileUri = data.data!!

                        glideLoader(this).loadUserPicture(mSelectedImgFileUri!!, tv_photoedit)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@userProfileEdit,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("Request cancelled", "Image selection cancelled")
            }
        }
    }

    private fun validateUserDetails(): Boolean {
        return when {
            mSelectedImgFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_image), true)
                false
            }
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_firstName), true)
                false
            }

            TextUtils.isEmpty(et_last_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_lastName), true)
                false
            }

            TextUtils.isEmpty(et_location.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_location), true)
                false
            }

            TextUtils.isEmpty(et_bio.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_bio    ), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun userProfileUpdateSuccess() {
        Toast.makeText(
            this@userProfileEdit,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@userProfileEdit, dashBoard::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }
}
