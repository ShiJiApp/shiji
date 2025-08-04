package com.vuforia.engine.native_sample.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vuforia.engine.native_sample.MainActivity
import com.vuforia.engine.native_sample.R

class LoginFragment : Fragment() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvToRegister: TextView
    private lateinit var rbAgreement: RadioButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        tvToRegister = view.findViewById(R.id.tvRegister)

        rbAgreement = view.findViewById(R.id.rbAgreement)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (!rbAgreement.isChecked) {
                AlertDialog.Builder(requireContext())
                    .setTitle("提示")
                    .setMessage("请勾选同意协议后再登录")
                    .setPositiveButton("确定") { dialog, _ -> dialog.dismiss() }
                    .show()
                return@setOnClickListener
            }

            val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val savedPassword = prefs.getString(username, null)

            if (savedPassword == password || username=="admin" && password == "123456") {
                Toast.makeText(requireContext(), "登录成功", Toast.LENGTH_SHORT).show()
                val sharedPref = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putBoolean("is_logged_in", true)
                editor.putString("current_user", username)
                editor.apply()

                val profilePrefs = requireContext().getSharedPreferences("profile_$username", Context.MODE_PRIVATE)
                with(profilePrefs.edit()) {
                    putString("nickname", username)
                    putString("description", "测试用户")
                    apply()
                }

                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "账号或密码错误", Toast.LENGTH_SHORT).show()
            }
        }

        tvToRegister.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("提示")
                .setMessage("本产品为内测版，请输入公用账号登录。")
                .setPositiveButton("知道了") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        return view
    }
}
