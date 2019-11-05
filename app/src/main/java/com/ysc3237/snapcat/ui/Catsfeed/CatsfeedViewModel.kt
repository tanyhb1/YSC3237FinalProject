package com.ysc3237.snapcat.ui.Catsfeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CatsfeedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Catsfeed Fragment"
    }
    val text: LiveData<String> = _text
}