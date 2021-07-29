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
import com.example.fluffrapp.models.pet
import com.example.fluffrapp.models.user
import com.example.fluffrapp.utils.glideLoader
import kotlinx.android.synthetic.main.activity_user_pet_profile_edit.*
import kotlinx.android.synthetic.main.activity_user_profile.*


import java.io.IOException

class userPetProfileEdit : baseActivity(), View.OnClickListener {
/*
    //private lateinit var mUserDetails: user
    private var mSelectedImgFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""
    private lateinit var mUserDetails: pet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Edit Pet Profile"
        setContentView(R.layout.activity_user_pet_profile_edit)

        if (intent.hasExtra(constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(constants.EXTRA_USER_DETAILS)!!
        }

        if (mUserDetails.profileCompleted == 0) {
            et_first_name.isEnabled = true
            et_first_name.setText(mUserDetails.petName)

            et_location.isEnabled = true
            et_location.setText(mUserDetails.location)

            et_bio.isEnabled = true
            et_bio.setText(mUserDetails.petBio)
        } else if (mUserDetails.profileCompleted == 1) {

            glideLoader(this@userPetProfileEdit).loadUserPicture(
                mUserDetails.petImage,
                iv_pet_photo
            )
            // Set the existing values to the UI and allow user to edit except the Email ID.
            et_first_name.isEnabled = true
            et_first_name.setText(mUserDetails.petName)

            et_location.isEnabled = true
            et_location.setText(mUserDetails.location)

            et_bio.isEnabled = true
            et_bio.setText(mUserDetails.petBio)

            if (mUserDetails.petGender == constants.MALE) {
                rb_male.isChecked = true
            } else {
                rb_female.isChecked = true
            }

        }

        tv_photoedit.setOnClickListener(this@userPetProfileEdit)
        btn_save.setOnClickListener(this@userPetProfileEdit)


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
                                this@userPetProfileEdit,
                                mSelectedImgFileUri,
                                constants.PET_PROF_IMAGE
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
        //var mUserDetails = user()
        val userHashMap = HashMap<String, Any>()

        val firstName = et_first_name.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.petName) {
            userHashMap[constants.PET_NAME] = firstName
        }

        val location = et_location.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.location) {
            userHashMap[constants.LOCATION] = location
        }

        val bio = et_bio.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.petBio) {
            userHashMap[constants.PET_BIO] = bio
        }

        val gender = if (rb_male.isChecked) {
            constants.MALE
        } else {
            constants.FEMALE
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[constants.PET_PROF_IMAGE] = mUserProfileImageURL
        }

        if (gender.isNotEmpty() && gender != mUserDetails.petGender) {
            userHashMap[constants.PET_GENDER] = gender
        }


        if (mUserDetails.profileCompleted == 0) {
            userHashMap[constants.COMPLETE_PROFILE] = 1
        }

        firestore().updateUserProfile(this@userPetProfileEdit, userHashMap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                constants.showImagChosen(this@userPetProfileEdit)
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

                        glideLoader(this@userPetProfileEdit).loadUserPicture(
                            mSelectedImgFileUri!!,
                            tv_photoedit
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@userPetProfileEdit,
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
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_petName), true)
                false
            }
            TextUtils.isEmpty(et_location.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_location), true)
                false
            }

            TextUtils.isEmpty(et_bio.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_bio), true)
                false
            }
            else -> {
                true
            }
        }


    }

    fun userProfileUpdateSuccess() {
        Toast.makeText(
            this@userPetProfileEdit,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@userPetProfileEdit, dashBoard::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }

    fun onRadioButtonClicked(view: View) {}*/
}
