package com.vuforia.engine.native_sample.adapter

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vuforia.engine.native_sample.R
import com.vuforia.engine.native_sample.entity.Guide

class GuideAdapter(
    private val guideList: List<Guide>,
    private val onGuideConfirmed: (Guide) -> Unit
) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null  // 适配器级别的MediaPlayer

    class GuideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val guideName: TextView = view.findViewById(R.id.guide_name)
        val guideInfo: TextView = view.findViewById(R.id.guide_info)
        val guideNum: TextView = view.findViewById(R.id.guide_num)
        val guideImage: ImageView = view.findViewById(R.id.guide_image)
        val playSoundContainer: View = view.findViewById(R.id.play_sound)
        val confirmGuideContainer: View = view.findViewById(R.id.confirm_guide)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_guide_card, parent, false)
        return GuideViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = guideList[position]
        holder.guideName.text = guide.name
        holder.guideInfo.text = guide.info
        holder.guideNum.text = guide.times.toString()
        holder.guideImage.setImageResource(guide.imageResId)

        holder.playSoundContainer.setOnClickListener {
            // 先释放之前的播放器
            mediaPlayer?.release()
            mediaPlayer = null

            // 创建并播放新的音频
            mediaPlayer = MediaPlayer.create(holder.itemView.context, guide.audioResId)
            mediaPlayer?.start()

            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
        }

        holder.confirmGuideContainer.setOnClickListener {
            onGuideConfirmed(guide)
        }
    }

    override fun getItemCount(): Int = guideList.size

    // 在适配器销毁时释放资源（如果需要）
    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
