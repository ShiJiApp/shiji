package com.vuforia.engine.native_sample.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.makeramen.roundedimageview.RoundedImageView
import com.vuforia.engine.native_sample.R
import com.vuforia.engine.native_sample.adapter.ImageAdapter
import com.vuforia.engine.native_sample.mall.ClassicSpotsActivity
import com.vuforia.engine.native_sample.mall.DigitalPetPurchaseActivity
import com.vuforia.engine.native_sample.mall.LeaveMessageActivity
import com.vuforia.engine.native_sample.widget.Circle
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator

class ShopFragment : Fragment() {

    var imageResIds = listOf(
        R.drawable.banner1,
        R.drawable.banner2
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.mall, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 默认适配器的Banner
        val banner = view.findViewById<Banner<Int, ImageAdapter>>(R.id.banner).apply {
            addBannerLifecycleObserver(viewLifecycleOwner)
            setIndicator(CircleIndicator(context))
            setAdapter(ImageAdapter(imageResIds))
        }

        val digitalPetView = view.findViewById<LinearLayout>(R.id.digital_pet_container)
        digitalPetView.setOnClickListener {
            val intent = Intent(requireContext(), DigitalPetPurchaseActivity::class.java)
            startActivity(intent)
        }

        val classic_spot = view.findViewById<RoundedImageView>(R.id.classic_spot)
        classic_spot.setOnClickListener {
            val intent = Intent(requireContext(), ClassicSpotsActivity::class.java)
            startActivity(intent)
        }

        val leave_msg = view.findViewById<Circle>(R.id.mall_leave_msg)
        leave_msg.setOnClickListener {
            val intent = Intent(requireContext(), LeaveMessageActivity::class.java)
            startActivity(intent)
        }
    }
}
