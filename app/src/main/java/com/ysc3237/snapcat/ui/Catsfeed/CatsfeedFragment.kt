package com.ysc3237.snapcat.ui.Catsfeed
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ysc3237.snapcat.MainActivity
import android.R
import com.google.android.gms.maps.model.LatLng


class CatsfeedFragment : Fragment() {

    companion object {
        lateinit var myAdapter1: myAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(com.ysc3237.snapcat.R.layout.fragment_catsfeed, container, false)

        val mRecyclerView = root.findViewById<RecyclerView>(com.ysc3237.snapcat.R.id.recyclerview)
        val mGridLayoutManager = GridLayoutManager(context, 2)
        mRecyclerView.layoutManager = mGridLayoutManager

        val mCatList: List<CatData>
        var mCatData: CatData

        mCatList = ArrayList()

        MainActivity.instance.initCats();

        myAdapter1 = myAdapter(context, MainActivity.catList)
//        MyAdapter(Context mContext, List< CatData > mCatList) {
        mRecyclerView.adapter = myAdapter1

        return root
    }

}