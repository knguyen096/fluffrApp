package com.example.fluffrapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class FRadio (context: Context, attributeSet: AttributeSet): AppCompatRadioButton(context,attributeSet) {
    init {
        applyfont()
    }
    private fun applyfont() {
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "TitilliumWeb-Regular.ttf")
        setTypeface(typeface)
    }

}