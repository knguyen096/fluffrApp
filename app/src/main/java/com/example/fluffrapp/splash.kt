package com.example.fluffrapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlin.math.log

class splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                startActivity(Intent(this@splash, login::class.java))
                finish()
            },
            1500
        )

       /* val typeface: Typeface = Typeface.createFromAsset(assets, "Prototype.ttf")
        tv_app_name.typeface = typeface*/
    }
}