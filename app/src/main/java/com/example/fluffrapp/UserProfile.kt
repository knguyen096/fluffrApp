package com.example.fluffrapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fluffrapp.adapters.ProfilePostAdapter
import com.example.fluffrapp.adapters.petAdapter
import com.example.fluffrapp.base.baseActivity
import com.example.fluffrapp.firestore.firestore
import com.example.fluffrapp.models.pet
import com.example.fluffrapp.models.user
import com.example.fluffrapp.models.post
import com.example.fluffrapp.utils.constants
import com.example.fluffrapp.utils.glideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*


class UserProfile : baseActivity()/*, View.OnClickListener*/ {
    private lateinit var mUserDetails: user
    private var mUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        if (intent.hasExtra(constants.EXTRA_USER_ID)) {
            mUserID = intent.getStringExtra(constants.EXTRA_USER_ID)!!
            Log.i("User ID", mUserID)
        }
        getUserDetails()

    }

    private fun getPetFirestore() {
        firestore().getPetList2(this)
    }

    fun successPetFirestore(petList: ArrayList<pet>) {
        rv_petlist.layoutManager = GridLayoutManager(this, 3)
        var adapter2 =
            petAdapter(this, petList)
        rv_petlist.adapter = adapter2
    }

    fun successPostProfileListFirestore2(postList: ArrayList<post>) {
        val receivedUserid = intent.getStringExtra(constants.EXTRA_USER_ID)

        rv_posts_items.layoutManager = LinearLayoutManager(this)
        var posts = ArrayList<post>()

        var adapter =
            ProfilePostAdapter(this, postList)
        rv_posts_items.adapter = adapter
    }

    private fun getCurrentUserDetails() {
        firestore().getCurrentUserDetails(this@UserProfile)
    }

    private fun getUserDetails() {
        firestore().getUserDetails(this, mUserID)
    }

    private fun getPostProfileListFirestore() {
        firestore().getPostProfileList2(this)
    }

    fun userDetailSuccess(user: user) {
        mUserDetails = user
        supportActionBar?.title = "${user.ownerFirstName} ${user.ownerLastName}"

        glideLoader(this@UserProfile).loadUserPicture(user.ownerImage, iv_user_photo)
        tv_name.text = "${user.ownerFirstName} ${user.ownerLastName}"
        tv_location.text = user.location
        tv_bio.text = user.ownerBio

    }

    override fun onResume() {
        super.onResume()
        getCurrentUserDetails()
        getPetFirestore()
        getPostProfileListFirestore()

    }

}