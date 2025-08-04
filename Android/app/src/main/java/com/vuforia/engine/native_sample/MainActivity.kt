/*===============================================================================
Copyright (c) 2024 PTC Inc. and/or Its Subsidiary Companies. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/

package com.vuforia.engine.native_sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.vuforia.engine.native_sample.databinding.ActivityMainBinding
import com.vuforia.engine.native_sample.fragment.ExploreFragment
import com.vuforia.engine.native_sample.fragment.HomeFragment
import com.vuforia.engine.native_sample.fragment.ProfileFragment
import com.vuforia.engine.native_sample.fragment.ShopFragment


/**
 * The MainActivity presents a simple choice for the user to select Image Targets or Model Targets.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fresco.initialize(this)
        // 初始化显示首页
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commit()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnNavigationItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_explore -> ExploreFragment()
                R.id.nav_shop -> ShopFragment()
                R.id.nav_profile -> ProfileFragment()
                R.id.nav_ar -> {
                    startActivity(Intent(this, ArActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                else -> null
            }

            fragment?.let { replaceFragment(it) }
            true
        }

        // 如果是从ArActivity返回的，读取参数并跳转
        val navigateTo = intent.getIntExtra("navigate_to", -1)
        if (navigateTo != -1) {
            bottomNav.selectedItemId = navigateTo
        } else {
            bottomNav.selectedItemId = R.id.nav_home // 默认加载首页
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        return true
    }





//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//        binding.btnAr.setOnClickListener {
//            val intent = Intent(this, VuforiaActivity::class.java)
//            intent.putExtra("Target", VuforiaActivity.getImageTargetId()) // 默认跳转 ImageTarget
//            startActivity(intent)
//        }
//    }


//    fun goToActivity(view: View) {
//        if (view.id == binding.btnImageTarget.id || view.id == binding.btnModelTarget.id) {
//
//            val intent = Intent(
//                this@MainActivity,
//                VuforiaActivity::class.java
//            )
//            if (view.id == binding.btnImageTarget.id) {
//                intent.putExtra("Target", VuforiaActivity.getImageTargetId())
//            } else {
//                intent.putExtra("Target", VuforiaActivity.getModelTargetId())
//            }
//
//            startActivity(intent)
//        }
//    }


    companion object {

        // Used to load the 'VuforiaSample' library on application startup.
        init {
            System.loadLibrary("VuforiaSample")
        }
    }

//    private lateinit var binding: ActivityMainBinding
}
