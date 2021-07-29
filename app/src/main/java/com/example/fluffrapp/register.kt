package com.example.fluffrapp.register

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fluffrapp.*
import com.example.fluffrapp.base.baseActivity
import com.example.fluffrapp.firestore.firestore
import com.example.fluffrapp.models.user
import com.example.fluffrapp.utils.constants
import com.example.fluffrapp.utils.glideLoader
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.et_first_name
import kotlinx.android.synthetic.main.activity_register.et_last_name

import java.io.IOException


class register : baseActivity(), View.OnClickListener {
    private var mSelectedImgFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.title = "Create an account"
        tv_login.setOnClickListener {
            val intent = Intent(this@register, login::class.java)
            startActivity(intent)
        }
        btn_register.setOnClickListener {
            registerUser()
         //  firestore().uploadImageToCloud(this, mSelectedImgFileUri, constants.POST_IMAGE)
        }

        //f1_user_imageRegis.setOnClickListener(this@register)
    }

    private fun validateRegisterDetails(): Boolean {
        return when {
          /*  mSelectedImgFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_image), true)
                false
            }*/
            TextUtils.isEmpty(
                et_first_name.text.toString()
                    .trim { it <= ' ' }) || et_first_name.length() <= 1 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_firstName), true)
                false
            }
            TextUtils.isEmpty(
                et_last_name.text.toString().trim { it <= ' ' }) || et_last_name.length() <= 1 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_lastName), true)
                false
            }
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(et_confirm_pass.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_confirm), true)
                false
            }

            et_password.text.toString().trim { it <= ' ' } != et_confirm_pass.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_mismatch), true)
                false
            }
            else -> {
                true
            }
        }
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
                            this@register,
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
    }*/

    private fun registerUser() {
        if (validateRegisterDetails()) {

            val email: String = et_email.text.toString().trim { it <= ' ' }
            val password: String = et_password.text.toString().trim { it <= ' ' }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = user(
                                firebaseUser.uid,
                                mUserProfileImageURL,
                                et_first_name.text.toString().trim { it <= ' ' },
                                et_last_name.text.toString().trim { it <= ' ' },
                                et_email.text.toString().trim { it <= ' ' }
                            )

                            firestore()
                                .registerUser(this@register, user)
                            finish()

                        } else
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                )
        }
    }

    fun userRegistrationSuccess() {
        Toast.makeText(
            this@register, resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()
    }

 /*   override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.f1_user_imageRegis -> {
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
            }
        }
    }*/

   /* override fun onRequestPermissionsResult(
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

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL



    }*/
  }
