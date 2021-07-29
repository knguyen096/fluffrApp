package com.example.fluffrapp

import android.os.Bundle
import android.util.Log
import com.example.fluffrapp.base.baseActivity
import com.example.fluffrapp.firestore.firestore
import com.example.fluffrapp.models.pet
import com.example.fluffrapp.utils.constants
import com.example.fluffrapp.utils.glideLoader
import kotlinx.android.synthetic.main.activity_create_pet.*
import kotlinx.android.synthetic.main.activity_user_profile.tv_bio
import kotlinx.android.synthetic.main.activity_user_profile.tv_name
import kotlinx.android.synthetic.main.pet_profile.*


class petProfile : baseActivity()/*, View.OnClickListener*/ {


    private lateinit var mPetDetails: pet
    private var mPetID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_profile)

        if (intent.hasExtra(constants.EXTRA_PET_ID)) {
            mPetID = intent.getStringExtra(constants.EXTRA_PET_ID)!!
            Log.i("Pet ID", mPetID)
        }
        getPetDetails()
    }

    private fun getCurrentPetDetails() {
        firestore().getCurrentUserDetails(this@petProfile)
    }

    private fun getPetDetails() {
        firestore().getPetDetails(this, mPetID)
    }


    fun petDetailSuccess(pet: pet) {
        mPetDetails = pet
        supportActionBar?.title = pet.petName

        glideLoader(this@petProfile).loadUserPicture(pet.petImage, iv_pet_photo)
        tv_name.text = pet.petName
        tv_bio.text = pet.petBio
        tv_gender.text = pet.petGender
        tv_likes.text = pet.likes
        tv_dislikes.text = pet.dislikes
        tv_age.text= pet.petAge


    }

    override fun onResume() {
        super.onResume()
        getCurrentPetDetails()

    }

}