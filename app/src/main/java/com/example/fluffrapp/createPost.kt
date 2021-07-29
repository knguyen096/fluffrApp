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
import com.example.fluffrapp.models.post
import com.example.fluffrapp.utils.constants
import com.example.fluffrapp.utils.glideLoader
import kotlinx.android.synthetic.main.activity_create_post.*
import java.io.IOException


class createPost : baseActivity(), View.OnClickListener {
    private var mSelectedImageFileURI: Uri? = null
    private var mPostImageURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        supportActionBar?.title = "Create new post"


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
                        constants.showImagChosen(this@createPost)
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
                        uploadPostImage()
                    }
                }
            }
        }
    }

    private fun uploadPostImage() {
        firestore().uploadImageToCloud(this, mSelectedImageFileURI, constants.POST_IMAGE)

    }

    fun imageUploadSuccess(imageURL: String) {
        mPostImageURL = imageURL

        uploadPostDetails()
    }

    fun postUploadSuccess() {
        Toast.makeText(
            this@createPost,
            resources.getString(R.string.post_upload_success),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    private fun uploadPostDetails() {
        val username = this.getSharedPreferences(constants.FLUFPREFERENCES, Context.MODE_PRIVATE)
            .getString(constants.LOGGED_IN_USERNAME, "")!!

        val ownerImage = this.getSharedPreferences(constants.FLUFPREFERENCES, Context.MODE_PRIVATE)
            .getString(constants.USER_PROF_IMAGE, "")!!

        val post = post(
            firestore().getCurrentUserID(),
            username,
            et_post_location.text.toString().trim { it <= ' ' },
            et_post_content.text.toString().trim { it <= ' ' },
            mPostImageURL,
            ownerImage,
            System.currentTimeMillis()/1000

        )
        firestore().uploadPostDetails(this, post)


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

            TextUtils.isEmpty(et_post_content.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_post_content), true)
                false
            }

            TextUtils.isEmpty(et_post_location.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_location), true)
                false
            }
            else -> true
        }
    }
}

