package com.vuforia.engine.native_sample.entity

data class Guide(
    val name: String,
    val info: String,
    val times: Int,
    val imageResId: Int,
    val audioResId: Int
)