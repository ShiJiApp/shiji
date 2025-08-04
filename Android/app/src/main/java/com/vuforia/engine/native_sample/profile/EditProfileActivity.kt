package com.vuforia.engine.native_sample.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.vuforia.engine.native_sample.R
import com.vuforia.engine.native_sample.widget.Circle
import java.io.FileNotFoundException
import java.io.InputStream

class EditProfileActivity : AppCompatActivity() {

    private lateinit var imageView: Circle
    private lateinit var nicknameEdit: EditText
    private lateinit var descriptionEdit: EditText
    private lateinit var saveButton: Button

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        imageView = findViewById(R.id.profile_image)
        nicknameEdit = findViewById(R.id.edit_nickname)
        descriptionEdit = findViewById(R.id.edit_description)
        saveButton = findViewById(R.id.save_button)

        loadProfile()

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        saveButton.setOnClickListener {
            saveProfile()
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK) // 设置结果
            finish() // 返回 Profile 页面
        }
    }

    private fun loadProfile() {
        val username = getSharedPreferences("user_data", Context.MODE_PRIVATE).getString("current_user", "")
        val prefs = getSharedPreferences("profile_$username", Context.MODE_PRIVATE)
        nicknameEdit.setText(prefs.getString("nickname", ""))
        descriptionEdit.setText(prefs.getString("description", ""))

        val uriString = prefs.getString("avatar_uri", null)
        if (uriString != null) {
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(Uri.parse(uriString))
                val bitmap = BitmapFactory.decodeStream(inputStream)
                imageView.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveProfile() {
        val username = getSharedPreferences("user_data", Context.MODE_PRIVATE).getString("current_user", "")
        val prefs = getSharedPreferences("profile_$username", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("nickname", nicknameEdit.text.toString())
        editor.putString("description", descriptionEdit.text.toString())
        if (selectedImageUri != null) {
            editor.putString("avatar_uri", selectedImageUri.toString())
        }
        editor.apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let { uri ->
                // 获取持久访问权限
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imageView.setImageBitmap(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
