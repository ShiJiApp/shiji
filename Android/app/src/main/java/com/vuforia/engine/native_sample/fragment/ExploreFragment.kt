package com.vuforia.engine.native_sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.vuforia.engine.native_sample.R

class ExploreFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_layout, container, false)
        view.findViewById<TextView>(R.id.tv_title).text = "您现在已在寻迹界面"
        return view
    }
}