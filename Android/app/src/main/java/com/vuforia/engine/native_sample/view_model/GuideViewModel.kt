package com.vuforia.engine.native_sample.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// SharedGuideViewModel.kt
class GuideViewModel : ViewModel() {
    val selectedGuideImageResId = MutableLiveData<Int>()
    val selectedGuideAudioResId = MutableLiveData<Int>()

    fun selectGuide(imageResId: Int, audioResId: Int) {
        selectedGuideImageResId.value = imageResId
        selectedGuideAudioResId.value = audioResId
    }
}
