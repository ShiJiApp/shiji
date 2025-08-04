package com.vuforia.engine.native_sample.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.ImageViewTarget
import com.vuforia.engine.native_sample.GuideActivity
import com.vuforia.engine.native_sample.R
import com.vuforia.engine.native_sample.adapter.WaterFallAdapter
import com.vuforia.engine.native_sample.entity.PostCard
import com.vuforia.engine.native_sample.view_model.GuideViewModel


class HomeFragment : Fragment() {
    private lateinit var foxImageView: ImageView
    private lateinit var guideNameTextView: TextView
    private lateinit var guideIntroTextView : TextView
    private var foxMediaPlayer: MediaPlayer? = null
    companion object {
        private var hasPlayedFoxOnce = false
    }
    private val guideActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageResId = data?.getIntExtra("selected_guide_image", -1) ?: -1
            if (imageResId != -1 && ::foxImageView.isInitialized) {
                foxImageView.setImageResource(imageResId)
            }
            val name = data?.getStringExtra("selected_guide_name") ?: "None"
            if (name != "None" && ::guideNameTextView.isInitialized) {
                guideNameTextView.text = name
                val guideIntro = "嗨，我是你的专属引导员$name，和我一起探索地图吧！"
                guideIntroTextView.text = guideIntro
            }
        }
    }

    private var mRecyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: WaterFallAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.home, container, false)

        val outerFrame: FrameLayout = view.findViewById(R.id.container)
        val innerLinear: LinearLayout = view.findViewById(R.id.find_hidden_elem)

        foxImageView = ImageView(requireContext())
        foxImageView.setImageResource(R.drawable.fox)
        guideNameTextView = view.findViewById(R.id.guide_name)
        guideIntroTextView  = view.findViewById(R.id.guideIntroTextView)

        // 创建fox.png对应的imageview
        outerFrame.post {
            val outerPos = IntArray(2)
            val innerPos = IntArray(2)

            outerFrame.getLocationOnScreen(outerPos)
            innerLinear.getLocationOnScreen(innerPos)

            Log.d("debug!!!", "innerPos[1] ${innerPos[1]} outerPos[1] ${outerPos[1]}")

            val height = innerPos[1] - outerPos[1]  // 高度 = 两层顶部距离

            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fox)
            val originalWidth = bitmap.width
            val originalHeight = bitmap.height
            val aspectRatio = originalWidth.toFloat() / originalHeight

            val width = (height * aspectRatio).toInt()  // 按比例计算宽度

            Log.d("debug!!!", "height $height width $width")

            // 设置布局参数
            val params = FrameLayout.LayoutParams(width, height)
            params.gravity = Gravity.END or Gravity.TOP
            params.marginEnd = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics
            ).toInt()
            foxImageView.layoutParams = params

            // 添加到 FrameLayout
            outerFrame.addView(foxImageView)

            if (hasPlayedFoxOnce) {
                return@post
            }

            playFoxAnimationAndSound()
            hasPlayedFoxOnce = true
        }

        // 设置打卡、门票补贴两个卡片对应的高度
        view.post {
            val leftCard = view.findViewById<CardView>(R.id.daka_card)
            val rightCard = view.findViewById<CardView>(R.id.butie_card)

            val leftHeight = leftCard.height
            val rightHeight = rightCard.height
            val heightDiff = leftHeight - rightHeight


            // 如果差值大于0，动态调整 _pic 的高度
            if (heightDiff > 0) {
                val pic1 = view.findViewById<ImageView>(R.id._pic1)
                val pic2 = view.findViewById<ImageView>(R.id._pic2)
                val pic3 = view.findViewById<ImageView>(R.id._pic3)

                val newSize = pic1.height + heightDiff

                val layoutParams1 = pic1.layoutParams
                layoutParams1.height = newSize
                layoutParams1.width = newSize
                pic1.layoutParams = layoutParams1

                val layoutParams2 = pic2.layoutParams
                layoutParams2.height = newSize
                layoutParams2.width = newSize
                pic2.layoutParams = layoutParams2

                val layoutParams3 = pic3.layoutParams
                layoutParams3.height = newSize
                layoutParams3.width = newSize
                pic3.layoutParams = layoutParams3
            }
        }

        // 设置post大小
//        view.post {
//            val card1 = view.findViewById<CardView>(R.id._blog_pic_cont1)
//            val card2 = view.findViewById<CardView>(R.id._blog_pic_cont2)
//            val image1 = view.findViewById<ImageView>(R.id.blog_pic1)
//            val image2 = view.findViewById<ImageView>(R.id.blog_pic2)
//
//            card1?.let {
//                val cardWidth = it.width
//                val drawable = image1?.drawable
//
//                if (drawable != null) {
//                    val drawableWidth = drawable.intrinsicWidth
//                    val drawableHeight = drawable.intrinsicHeight
//
//                    if (drawableWidth > 0 && drawableHeight > 0) {
//                        val aspectRatio = drawableHeight.toFloat() / drawableWidth.toFloat()
//                        val newHeight = (cardWidth * aspectRatio).toInt()
//
//                        val layoutParams = image1.layoutParams
//                        layoutParams.width = cardWidth
//                        layoutParams.height = newHeight
//                        image1.layoutParams = layoutParams
//                    }
//                }
//            }
//            card2?.let {
//                val cardWidth = it.width
//                val drawable = image2?.drawable
//
//                if (drawable != null) {
//                    val drawableWidth = drawable.intrinsicWidth
//                    val drawableHeight = drawable.intrinsicHeight
//
//                    if (drawableWidth > 0 && drawableHeight > 0) {
//                        val aspectRatio = drawableHeight.toFloat() / drawableWidth.toFloat()
//                        val newHeight = (cardWidth * aspectRatio).toInt()
//
//                        val layoutParams = image2.layoutParams
//                        layoutParams.width = cardWidth
//                        layoutParams.height = newHeight
//                        image2.layoutParams = layoutParams
//                    }
//                }
//            }
//        }

        val changeGuideLayout = view.findViewById<LinearLayout>(R.id.change_guide)
        changeGuideLayout.setOnClickListener {
            val intent = Intent(requireContext(), GuideActivity::class.java)
            guideActivityLauncher.launch(intent)
        }

        mLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView = view.findViewById<RecyclerView>(R.id.home_postcard)
        mRecyclerView?.layoutManager = mLayoutManager
        mAdapter = WaterFallAdapter(requireContext(), buildData())
        mRecyclerView?.adapter = mAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedViewModel = ViewModelProvider(requireActivity()).get(GuideViewModel::class.java)

        sharedViewModel.selectedGuideImageResId.observe(viewLifecycleOwner) { imageResId ->
            Log.d("debug!!!", "HomeFragment 收到 imageResId: $imageResId")
            if (::foxImageView.isInitialized && imageResId != null) {
                foxImageView.setImageResource(imageResId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        foxMediaPlayer?.release()
        foxMediaPlayer = null
    }


    private fun buildData(): List<PostCard> {
        val names = arrayOf("黄鹤楼", "湖北省博物馆", "晴川阁", "武汉两江浏览", "东湖", "如果有一件事是重要的")
        val imgUrs = arrayOf(R.drawable.spot_huanghelou, R.drawable.spot_bowuguan,
            R.drawable.spot_qingchuange, R.drawable.spot_liangjiangliulan, R.drawable.spot_donghu
        )
        val content = arrayOf(
            "还行吧，来之前很期待，来之后感觉也没有太惊艳，门票价格不贵，周围吃的也多，景色也不错，周末人太多了，听说晚上还有灯光秀，当时晚上没有时间去看了",
            "荆楚大地的历史文化，各种不同的历史文化时期，各类不同类型的历史文物，都在诉说这片大地上曾经发生过的故事，战争，人性，天才的灵光一闪，普通人的随波逐流，如今都汇聚在这小小一栋建筑之中，供后人学习，瞻仰",
            "晴川阁这个景点是免费参观，不需要买门票的。占地不算非常大，但是春天去走一走看一看很舒服，见到不少新人到这里拍婚纱照，就知道这里景色还不错。",
            "好耶！ 风景很美，不愧是我同学推荐的看夜景的好地方！ 船上话说他鸣笛居然是喇叭发出来的，我一直以为是那种，就那种气流的声音，结果居然是喇叭发出的，真的是有趣233 【景色】5+ 【趣味】4 【性价比】4",
            "让人有一种很舒服的感觉，走在桥上时候，迎着风，感觉不一般。让我很想留在武汉，面对着黄鹤楼背对着武汉大学，有如它见证了武汉的发展。无论步行或骑自行车在那逛逛都会觉得那里很棒的哦。我相信我还会在去那里吹吹风，散散心",
        )

        val list: MutableList<PostCard> = ArrayList<PostCard>()
        for (i in 0..4) {
            val c: PostCard = PostCard()
            c.postId = i
            c.resId = imgUrs[i]
            c.name = names[i]
            c.content = content[i]
            c.imgHeight = (i % 3) * 100 + 400 //偶数和奇数的图片设置不同的高度，以到达错开的目的
            list.add(c)
        }

        return list
    }

    private fun playFoxAnimationAndSound() {
        Glide.with(this)
            .asGif()
            .load(R.drawable.ani_fox)
            .into(object : ImageViewTarget<GifDrawable>(foxImageView) {
                override fun setResource(resource: GifDrawable?) {
                    resource ?: return
                    foxImageView.setImageDrawable(resource)
                    resource.setLoopCount(2)
                    resource.start()

                    foxMediaPlayer = MediaPlayer.create(requireContext(), R.raw.audio_fox).apply {
                        start()
                        setOnCompletionListener {
                            updateToStatic()
                            it.release()
                            foxMediaPlayer = null
                        }
                    }
                }
            })
    }

    private fun updateToStatic() {
        foxImageView.setImageResource(R.drawable.fox)
    }
}
