package com.example.fluffrapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class FEditText (context: Context, attrs: AttributeSet): AppCompatEditText(context,attrs) {

    init {
        applyfont()
    }

    private fun applyfont() {
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "TitilliumWeb-Regular.ttf")
        setTypeface(typeface)
    }
}