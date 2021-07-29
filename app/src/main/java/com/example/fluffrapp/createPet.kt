package com.example.fluffrapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fluffrapp.base.baseActivity
import com.example.fluffrapp.firestore.firestore
import com.example.fluffrapp.models.pet
import com.example.fluffrapp.utils.constants
import com.example.fluffrapp.utils.glideLoader
import kotlinx.android.synthetic.main.activity_create_pet.*
import java.io.IOException


class createPet : baseActivity(), View.OnClickListener {
    private var mSelectedImageFileURI: Uri? = null
    private var mPetImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pet)
        supportActionBar?.title = "Create Pet Profile"



        iv_post_image.setOnClickListener(this)
        btn_post.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_post_image -> {
                    if (ContextCompat.checkSelfPermission(
                            this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        constants.showImagChosen(this@createPet)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_post -> {
                    if (validatePostDetails()) {
                        uploadPetImage()
                    }
                }
            }
        }
    }

    private fun uploadPetImage() {
        firestore().uploadImageToCloud(this, mSelectedImageFileURI, constants.PET_PROF_IMAGE)

    }

    fun imageUploadSuccess(imageURL: String) {
        mPetImageURL = imageURL

        uploadPetDetails()
    }

    fun petUploadSuccess() {
        Toast.makeText(
            this@createPet,
            resources.getString(R.string.pet_upload_success),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    private fun uploadPetDetails() {
        val gender = if (rb_male.isChecked) {
            constants.MALE
        } else {
            constants.FEMALE
        }
        val post = pet(
            firestore().getCurrentUserID(),
            mPetImageURL,
            et_petName.text.toString().trim { it <= ' ' },
            et_petBio.text.toString().trim { it <= ' ' },
            gender,
            et_petAge.text.toString().trim { it <= ' ' },
            et_petLikes.text.toString().trim { it <= ' ' },
            et_petDisikes.text.toString().trim { it <= ' ' }

        )
        firestore().uploadPetDetails(this, post)


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
                    iv_post_image.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_edit
                        )
                    )
                    mSelectedImageFileURI = data.data!!
                    try {
                        glideLoader(this).loadUserPicture(mSelectedImageFileURI!!, iv_post_image)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("Request cancelled", "Image selection cancelled")
            }
        }
    }

    private fun validatePostDetails(): Boolean {
        return when {
            mSelectedImageFileURI == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_image), true)
                false
            }

            TextUtils.isEmpty(et_petName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_petName), true)
                false
            }

            TextUtils.isEmpty(et_petBio.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_bio), true)
                false
            }

            TextUtils.isEmpty(et_petLikes.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_like), true)
                false
            }

            TextUtils.isEmpty(et_petDisikes.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_dislike), true)
                false
            }

            TextUtils.isEmpty(et_petAge.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_age), true)
                false
            }
            else -> true
        }
    }
}

