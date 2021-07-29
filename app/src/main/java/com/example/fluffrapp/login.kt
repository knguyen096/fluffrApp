package com.example.fluffrapp

import android.content.Intent

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.fluffrapp.base.baseActivity
import com.example.fluffrapp.firestore.firestore
import com.example.fluffrapp.models.user
import com.example.fluffrapp.register.register
import com.example.fluffrapp.utils.constants

import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class login : baseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)

    }

    fun userLoggedInSuccess(user: user) {

        if (user.profileCompleted == 0) {
            val intent = Intent(this@login, userProfileEdit::class.java)
            intent.putExtra(constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            startActivity(Intent(this@login, dashBoard::class.java))
        }
        finish()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_login -> {
                    loginUser()
                }
                R.id.tv_register -> {
                    val intent = Intent(this@login, register::class.java)
                    startActivity(intent)
                }
            }
        }
    }


    private fun loginUser() {
        if (validateLoginDetails()) {

            val email: String = et_email.text.toString().trim { it <= ' ' }
            val password: String = et_password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        firestore().getCurrentUserDetails(this@login)
                    } else
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(
                et_email.text.toString()
                    .trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(
                et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }
}