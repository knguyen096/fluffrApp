package com.example.fluffrapp.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import  android.graphics.Typeface

class FButton(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs){
    init {
        applyFont()
    }

    private fun applyFont() {
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "TitilliumWeb-Bold.ttf")
        setTypeface(typeface)
    }
}