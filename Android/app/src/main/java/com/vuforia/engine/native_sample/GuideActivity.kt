package com.vuforia.engine.native_sample


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vuforia.engine.native_sample.adapter.GuideAdapter
import com.vuforia.engine.native_sample.entity.Guide

class GuideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_guide)

        val guideList = listOf(
            Guide("小千年", "我是时迹伴游小千年，来体验趣味旅游吧！", 1021, R.drawable.fox, R.raw.audio_fox),
            Guide("莉莉", "我是专业导游莉莉，和我一起开启知识丰富的旅游~", 1211, R.drawable.guide1, R.raw.audio_guide1),
            Guide("马克斯", "Hi，我是马克斯，想和我一起开始驴友游戏吗？", 160, R.drawable.guide2, R.raw.audio_guide2),
            Guide("柳云飞", "我是柳云飞，让我们一起探索此方佳境吧！", 60, R.drawable.guide3, R.raw.audio_guide3),
            // 可继续添加
        )

        val adapter = GuideAdapter(guideList) { selectedGuide ->
            val resultIntent = Intent().apply {
                putExtra("selected_guide_image", selectedGuide.imageResId)
                putExtra("selected_guide_audio", selectedGuide.audioResId)
                putExtra("selected_guide_name", selectedGuide.name)
            }
            setResult(RESULT_OK, resultIntent)
            finish()  // 返回 Home 界面
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv_guides)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val backButton = findViewById<ImageView>(R.id.iv_back_arrow)
        backButton.setOnClickListener {
            finish() // 关闭当前 Activity，返回上一个界面
        }
    }

}