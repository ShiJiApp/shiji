package com.vuforia.engine.native_sample.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.vuforia.engine.native_sample.PostDetailActivity
import com.vuforia.engine.native_sample.R
import com.vuforia.engine.native_sample.entity.PostCard

class WaterFallAdapter(
    private val context: Context,
    private val data: List<PostCard> // 使用 val 声明不可变属性
) : RecyclerView.Adapter<WaterFallAdapter.MyViewHolder>() { // 明确指定 ViewHolder 泛型

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val card = data[position] // 直接使用下标访问
        holder.userAvatar.run {
            setImageURI(Uri.parse("res://${context.packageName}/${card.resId}"))
            layoutParams.height = card.imgHeight // 动态设置高度
        }
        holder.userName.text = card.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PostDetailActivity::class.java)
            intent.putExtra("post_title", card.name)
            intent.putExtra("post_id", card.postId)
            intent.putExtra("post_image", card.resId)
            intent.putExtra("post_content", card.content)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = data.size // 单表达式函数

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userAvatar: SimpleDraweeView = itemView.findViewById(R.id.user_avatar)
        val userName: TextView = itemView.findViewById(R.id.user_name)
    }
}