package com.vuforia.engine.native_sample

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ArActivity : AppCompatActivity() {
    private lateinit var previewView: PreviewView
    private lateinit var gifOverlay: ImageView
    private lateinit var hintText: TextView
    private lateinit var stickerContainer: FrameLayout
    private var lensFacing = CameraSelector.LENS_FACING_BACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_camera)

        previewView = findViewById(R.id.preview_view)
        gifOverlay = findViewById(R.id.gif_overlay)
        stickerContainer = findViewById(R.id.sticker_container)
        hintText = findViewById(R.id.ar_hint_text)

        val scanButton = findViewById<ImageView>(R.id.camera_scan)
        scanButton.setOnClickListener {
            val intent = Intent(this, VuforiaActivity::class.java)
            intent.putExtra("Target", VuforiaActivity.getImageTargetId()) // 默认跳转 ImageTarget
            startActivity(intent)
        }

        val stickerPanel = findViewById<LinearLayout>(R.id.sticker_panel)
        val stickerResIds = listOf(
            R.drawable.baijin,
            R.drawable.fox1,
            R.drawable.guide1,
            R.drawable.guide2,
            R.drawable.guide3
        )
        for (resId in stickerResIds) {
            val stickerIcon = ImageView(this).apply {
                setImageResource(resId)
                layoutParams = LinearLayout.LayoutParams(120, 120).apply {
                    setMargins(8, 8, 8, 8)
                }
                setPadding(10, 10, 10, 10)
                setOnClickListener { view ->
                    // 点击后生成可拖动贴纸
                    generateStickerAtViewPosition(view, resId)
                }
            }
            stickerPanel.addView(stickerIcon)
        }

        val openGalleryBtn = findViewById<ImageView>(R.id.camera_album)
        openGalleryBtn.setOnClickListener {
            openGallery()
        }

        // 加载 GIF 图
        Glide.with(this)
            .asGif()
            .load(R.drawable.fox1)
            .into(gifOverlay)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), 10
            )
        }

        // 显示提示 3 秒后弹出，1 秒后消失
        Handler(Looper.getMainLooper()).postDelayed({
            hintText.text = "您现在位置无趣味场景，可以跟着我走一走噢"
            hintText.visibility = View.VISIBLE

            // 1 秒后隐藏
            Handler(Looper.getMainLooper()).postDelayed({
                hintText.visibility = View.GONE
            }, 5000)

        }, 5000)

        val switchCameraButton: ImageView = findViewById(R.id.camera_switch)
        switchCameraButton.setOnClickListener {
            // 切换前后摄像头
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
            startCamera()
        }

        val moreButton = findViewById<ImageView>(R.id.camera_more)
        moreButton.setOnClickListener {
            stickerPanel.visibility =
                if (stickerPanel.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        val captureButton = findViewById<ImageView>(R.id.camera_snap)
        captureButton.setOnClickListener {
            takeScreenshotAndSave()
        }

        val backButton = findViewById<ImageView>(R.id.camera_back)
        backButton.setOnClickListener {
            finish() // 关闭当前 Activity，返回上一个界面
        }

        val homeButton = findViewById<ImageView>(R.id.camera_home)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra("navigate_to", R.id.nav_home) // 带参数
            startActivity(intent)
            finish()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivity(intent)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (exc: Exception) {
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    fun generateStickerAtViewPosition(clickedView: View, drawableRes: Int) {
        val location = IntArray(2)
        clickedView.getLocationOnScreen(location)
        val stickerPanelX = location[0]
        val stickerPanelY = location[1]

        val containerLocation = IntArray(2)
        stickerContainer.getLocationOnScreen(containerLocation)
        val containerX = containerLocation[0]
        val containerY = containerLocation[1]

        val relativeX = (stickerPanelX - containerX).toFloat()
        val relativeY = (stickerPanelY - containerY).toFloat()

        // 贴纸 ImageView
        val sticker = ImageView(this).apply {
            setImageResource(drawableRes)
            layoutParams = FrameLayout.LayoutParams(200, 200)
        }

        // 包裹贴纸的独立容器
        val stickerWrapper = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(200, 200)
            x = relativeX
            y = relativeY
            addView(sticker)
        }

        val scaleDetector = ScaleGestureDetector(this, ScaleListener(stickerWrapper))
        val touchListener = StickerTouchListener(stickerWrapper)

        stickerWrapper.setOnTouchListener { v, event ->
            val scaleHandled = scaleDetector.onTouchEvent(event)
            val touchHandled = touchListener.onTouch(v, event)
            scaleHandled || touchHandled
        }

        stickerContainer.addView(stickerWrapper)
    }


    fun takeScreenshotAndSave() {
        // 异步获取摄像头预览图
        val previewBitmap = previewView.bitmap
        if (previewBitmap == null) {
            Toast.makeText(this, "摄像头画面尚未准备好", Toast.LENGTH_SHORT).show()
            return
        }

        // 新建一个 bitmap 以叠加贴纸
        val resultBitmap = Bitmap.createBitmap(previewBitmap.width, previewBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

        // 先画摄像头画面
        canvas.drawBitmap(previewBitmap, 0f, 0f, null)

        // 按比例缩放贴纸容器到 previewBitmap 尺寸
        val scaleX = previewBitmap.width.toFloat() / previewView.width
        val scaleY = previewBitmap.height.toFloat() / previewView.height

        canvas.save()
        canvas.scale(scaleX, scaleY)

        // 画贴纸层（stickerContainer）
        stickerContainer.draw(canvas)
        canvas.restore()

        saveBitmapToGallery(resultBitmap)
    }

    fun saveBitmapToGallery(bitmap: Bitmap) {
        val filename = "sticker_photo_${System.currentTimeMillis()}.jpg"
        val fos: OutputStream?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyStickers")
            }
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "拍照成功，已保存", Toast.LENGTH_SHORT).show()
        }
    }


    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    inner class StickerTouchListener(private val view: View) : View.OnTouchListener {
        private var dX = 0f
        private var dY = 0f
        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(view.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    (view.parent as? FrameLayout)?.removeView(view)
                    return true
                }
            })
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            gestureDetector.onTouchEvent(event)

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dX = event.x
                    dY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = v.x + event.x - dX
                    val newY = v.y + event.y - dY
                    v.x = newX
                    v.y = newY
                }
            }

            return true
        }
    }

    inner class ScaleListener(private val targetView: View) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var scaleFactor = 1.0f

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleChange = detector.scaleFactor
            val speedMultiplier = 1.5f // 你可以调成 2.0f、3.0f 测试反应更快

//            scaleFactor *= Math.pow(scaleChange.toDouble(), speedMultiplier.toDouble()).toFloat()
            scaleFactor *= scaleChange
            scaleFactor = scaleFactor.coerceIn(0.3f, 3.0f) // 限制缩放范围

            targetView.scaleX = scaleFactor
            targetView.scaleY = scaleFactor
            return true
        }
    }
}