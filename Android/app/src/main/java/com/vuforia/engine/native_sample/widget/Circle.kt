package com.vuforia.engine.native_sample.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView

class Circle @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mPaint: Paint = Paint()
    private var mRadius: Int = 0
    private var mScale: Float = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 由于是圆形，宽高应保持一致
        val size = measuredWidth.coerceAtMost(measuredHeight)
        mRadius = size / 2
        setMeasuredDimension(size, size)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val drawable = drawable

        if (drawable != null && drawable is BitmapDrawable) {
            val originalBitmap = drawable.bitmap

            // 裁剪出图片中心的正方形区域
            val size = originalBitmap.width.coerceAtMost(originalBitmap.height)
            val x = (originalBitmap.width - size) / 2
            val y = (originalBitmap.height - size) / 2
            val squareBitmap = Bitmap.createBitmap(originalBitmap, x, y, size, size)

            // 将裁剪后的正方形缩放到 View 的大小
            val scaledBitmap = Bitmap.createScaledBitmap(squareBitmap, width, height, true)

            val shader = BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            mPaint.shader = shader

            // 画圆
            val radius = width.coerceAtMost(height) / 2f
            canvas.drawCircle(width / 2f, height / 2f, radius, mPaint)
        } else {
            super.onDraw(canvas)
        }
    }
}