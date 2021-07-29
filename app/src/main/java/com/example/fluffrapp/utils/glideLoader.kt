package com.example.fluffrapp.utils

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.fluffrapp.R
import com.example.fluffrapp.userProfileEdit
import java.io.IOException

class glideLoader(val context: Context) {
    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.flufprof)
                .into(imageView)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadPostPicture(image: Any, imageView: ImageView) {
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .into(imageView)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadUserPictureFragment(image: Any, imageView: ImageView) {
        try {
            Glide
                .with(Fragment())
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.flufprof)
                .into(imageView)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}