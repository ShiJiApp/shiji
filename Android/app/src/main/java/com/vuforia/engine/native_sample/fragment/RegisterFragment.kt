package com.vuforia.engine.native_sample.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vuforia.engine.native_sample.R

class RegisterFragment: Fragment() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirm: EditText
    private lateinit var btnRegister: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirm = view.findViewById(R.id.etConfirm)
        btnRegister = view.findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val confirm = etConfirm.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "账号和密码不能为空", Toast.LENGTH_SHORT).show()
            } else if (password != confirm) {
                Toast.makeText(context, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
            } else {
                val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                if (prefs.contains(username)) {
                    Toast.makeText(context, "账号已存在", Toast.LENGTH_SHORT).show()
                } else {
                    prefs.edit().putString(username, password).apply()
                    Toast.makeText(context, "注册成功，请登录", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack() // 返回登录界面
                }
            }
        }

        return view
    }
}
