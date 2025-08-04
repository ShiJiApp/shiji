package com.vuforia.engine.native_sample.mall

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.vuforia.engine.native_sample.R

class DigitalPetPurchaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_digital_pat_purchase)

//        setBg()
    }

    private fun setBg() {
        val idList = listOf(R.id.pet1, R.id.pet2, R.id.pet3)

        idList.forEach { id ->
            val boxLayout = findViewById<LinearLayout>(id)
            boxLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    boxLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    val width = boxLayout.width
                    val height = boxLayout.height

                    val original = BitmapFactory.decodeResource(resources, R.drawable.box_bg)
                    val scaled = Bitmap.createScaledBitmap(original, width, height, true)

                    val drawable = BitmapDrawable(resources, scaled)
                    boxLayout.background = drawable
                }
            })
        }
    }
}