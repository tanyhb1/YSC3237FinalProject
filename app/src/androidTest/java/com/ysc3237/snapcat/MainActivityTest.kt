package com.ysc3237.snapcat

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.ysc3237.snapcat.MainActivity.Companion.catList
import com.ysc3237.snapcat.MainActivity.Companion.catListLoaded
import com.ysc3237.snapcat.ui.Catsfeed.CatData
import org.json.JSONArray
import org.junit.Test

import org.junit.Assert.*
import java.util.ArrayList

class MainActivityTest {

    @Test
    fun initCats() {
        val catList = ArrayList<CatData>()
        var catListLoaded = false;
        !catListLoaded
    }

    @Test
    fun loadCats() {
        !catListLoaded
    }

    @Test
    fun loadDummyData(){
        !catList.isEmpty()
    }

}