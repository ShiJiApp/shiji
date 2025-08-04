package com.vuforia.engine.native_sample.mall

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.vuforia.engine.native_sample.R

class LeaveMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_message)

        val checkAll = findViewById<CheckBox>(R.id.visible_all)
        val checkFriends = findViewById<CheckBox>(R.id.visible_friends)
        val checkSelf = findViewById<CheckBox>(R.id.visible_self)

        checkAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkFriends.isChecked = false
                checkSelf.isChecked = false
            }
        }

        checkFriends.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkAll.isChecked = false
                checkSelf.isChecked = false
            }
        }

        checkSelf.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkAll.isChecked = false
                checkFriends.isChecked = false
            }
        }
    }
}