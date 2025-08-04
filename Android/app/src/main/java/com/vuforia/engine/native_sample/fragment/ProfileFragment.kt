package com.vuforia.engine.native_sample.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.vuforia.engine.native_sample.R
import com.vuforia.engine.native_sample.profile.EditProfileActivity

class ProfileFragment : Fragment() {
//    private val editProfileLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val user_name = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE).getString("current_user", "")
//            loadUserProfile(user_name) // 用户修改完成，刷新头像和昵称等
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
        val currentUser = sharedPref.getString("current_user", "")

        if (!isLoggedIn) {
            view.findViewById<TextView>(R.id.login_register_text)?.setOnClickListener {
                // 跳转到登录界面
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, LoginFragment())
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            view.findViewById<TextView>(R.id.login_register_text)?.setOnClickListener(null)
        }

        loadUserProfile(currentUser)


//        val textView = view.findViewById<TextView>(R.id.nickname_text)
//        val imageView = view.findViewById<ImageView>(R.id.modify)
//
//        textView.post {
//            val textHeight = textView.height
//            val imageHeight = (textHeight * 0.9).toInt()
//            val params = imageView.layoutParams
//            params.height = imageHeight
//            imageView.layoutParams = params
//        }

//        val modifyIcon = view.findViewById<ImageView>(R.id.modify)
//        modifyIcon.setOnClickListener {
//            val intent = Intent(requireContext(), EditProfileActivity::class.java)
//            editProfileLauncher.launch(intent)
//        }
    }

    private fun loadUserProfile(username: String?) {
        try {
            val prefs = requireContext().getSharedPreferences("profile_$username", Context.MODE_PRIVATE)
            val nickname = prefs.getString("nickname", "登录/注册")
            val description = prefs.getString("description", "快来登录，一起解锁打卡吧~")
            val avatarUriStr = prefs.getString("avatar_uri", null)

            view?.findViewById<TextView>(R.id.login_register_text)?.text = nickname
            view?.findViewById<TextView>(R.id.user_description)?.text = description

            val avatarView = view?.findViewById<ImageView>(R.id.user_avatar)
            if (!avatarUriStr.isNullOrEmpty()) {
                try {
                    val avatarUri = Uri.parse(avatarUriStr)
                    val inputStream = requireContext().contentResolver.openInputStream(avatarUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    avatarView?.setImageBitmap(bitmap ?: throw Exception("头像为空"))
                } catch (e: Exception) {
                    e.printStackTrace()
                    avatarView?.setImageResource(R.drawable.default_avatar) // 加载默认头像
                }
            } else {
                avatarView?.setImageResource(R.drawable.default_avatar) // 头像路径为空时
            }
        } catch (e: Exception) {
            e.printStackTrace()
//            showError("加载用户信息失败：${e.message}")
        }
    }

//    private fun showError(message: String?) {
//        val errorTextView = view?.findViewById<TextView>(R.id.error_message)
//        errorTextView?.visibility = View.VISIBLE
//        errorTextView?.text = "\u26A0\uFE0F 错误信息：$message"
//    }

    override fun onResume() {
        super.onResume()
        val sharedPref = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
        val currentUser = sharedPref.getString("current_user", "")

        view?.findViewById<TextView>(R.id.login_register_text)?.let { loginText ->
            if (isLoggedIn) {
                loginText.setOnClickListener(null)
                loginText.isClickable = false
            }
        }

        loadUserProfile(currentUser)
    }
}
