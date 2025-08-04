package com.vuforia.engine.native_sample.mall


import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.vuforia.engine.native_sample.R

class ClassicSpotsActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classic_spots)

        val container = findViewById<LinearLayout>(R.id.classic_spot_container)

        val title = listOf("黄鹤楼", "湖北省博物馆", "晴川阁", "武汉两江浏览", "东湖")
        val img = listOf(R.drawable.spot_huanghelou, R.drawable.spot_bowuguan,
            R.drawable.spot_qingchuange, R.drawable.spot_liangjiangliulan, R.drawable.spot_donghu)
        val high_light = listOf("亮点：诗词绝句中的江南名楼",
            "亮点：看镇馆之宝感受古楚文化",
            "亮点：要看武汉三镇与黄鹤楼",
            "亮点：饱览江城两岸好风光",
            "亮点：湖边漫步赏迷人湖景")



        for (i in 0 until 5) {
            val view = layoutInflater.inflate(R.layout.item_classic_spot, container, false)
            view.findViewById<ImageView>(R.id.spot_image).setImageResource(img[i])
            view.findViewById<TextView>(R.id.spot_highlight).text = high_light[i]
            view.findViewById<TextView>(R.id.spot_rank).text = "No. ${i+1}"
            view.findViewById<TextView>(R.id.spot_title).text = title[i]

            container.addView(view)
        }
    }
}