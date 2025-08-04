package com.vuforia.engine.native_sample

import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vuforia.engine.native_sample.R

class PostDetailActivity : AppCompatActivity() {

    private var liked = false
    private var likeCount = 0

    private var collected = false
    private var collectCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        // 获取传递的数据
        val postId = intent.getStringExtra("post_id") ?: 0
        val title = intent.getStringExtra("post_title") ?: "帖子标题"
        val content = intent.getStringExtra("post_content") ?: "帖子内容..."
        val imageResId = intent.getIntExtra("post_image", R.drawable.baijin)

        // 绑定视图
        val postImage = findViewById<ImageView>(R.id.post_image)
        val postTitle = findViewById<TextView>(R.id.post_title)
        val postContent = findViewById<TextView>(R.id.post_content)

        val btnLike = findViewById<ImageButton>(R.id.btn_like)
        val likeCountText = (btnLike.parent as? android.view.ViewGroup)?.getChildAt(1) as? TextView

        val btnCollect = findViewById<ImageButton>(R.id.btn_collect)
        val collectCountText = (btnCollect.parent as? android.view.ViewGroup)?.getChildAt(1) as? TextView

        val btnComment = findViewById<ImageButton>(R.id.btn_comment)

        postImage.setImageResource(imageResId)
        postTitle.text = title
        postContent.text = content

        // 从本地读取状态
        val prefs = getSharedPreferences("PostPrefs", Context.MODE_PRIVATE)
        liked = prefs.getBoolean("${postId}_liked", false)
        likeCount = prefs.getInt("${postId}_like_count", 0)

        collected = prefs.getBoolean("${postId}_collected", false)
        collectCount = prefs.getInt("${postId}_collect_count", 0)

        // 设置图标和数量
        btnLike.setImageResource(if (liked) R.drawable.like else R.drawable.unlike)
        likeCountText?.text = likeCount.toString()

        btnCollect.setImageResource(if (collected) R.drawable.collected else R.drawable.uncollected)
        collectCountText?.text = collectCount.toString()

        // 点赞按钮
        btnLike.setOnClickListener {
            liked = !liked
            if (liked) {
                likeCount++
                btnLike.setImageResource(R.drawable.like)
                Toast.makeText(this, "已点赞", Toast.LENGTH_SHORT).show()
            } else {
                likeCount--
                btnLike.setImageResource(R.drawable.unlike)
                Toast.makeText(this, "已取消点赞", Toast.LENGTH_SHORT).show()
            }
            likeCountText?.text = likeCount.toString()

            // 保存状态
            prefs.edit()
                .putBoolean("${postId}_liked", liked)
                .putInt("${postId}_like_count", likeCount)
                .apply()
        }

        // 收藏按钮
        btnCollect.setOnClickListener {
            collected = !collected
            if (collected) {
                collectCount++
                btnCollect.setImageResource(R.drawable.collected)
                Toast.makeText(this, "已收藏", Toast.LENGTH_SHORT).show()
            } else {
                collectCount--
                btnCollect.setImageResource(R.drawable.uncollected)
                Toast.makeText(this, "已取消收藏", Toast.LENGTH_SHORT).show()
            }
            collectCountText?.text = collectCount.toString()

            // 保存状态
            prefs.edit()
                .putBoolean("${postId}_collected", collected)
                .putInt("${postId}_collect_count", collectCount)
                .apply()
        }

        // 评论按钮
        btnComment.setOnClickListener {
            Toast.makeText(this, "进入评论区", Toast.LENGTH_SHORT).show()
        }
    }
}
