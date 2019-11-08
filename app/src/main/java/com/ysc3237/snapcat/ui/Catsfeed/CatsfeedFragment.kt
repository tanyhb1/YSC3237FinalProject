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




class CatsfeedFragment : Fragment() {

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

        mCatData = CatData(
            "Rose", getString(com.ysc3237.snapcat.R.string.description_flower_rose),
            com.ysc3237.snapcat.R.drawable.cat1
        )

        mCatList.add(mCatData)
        mCatData = CatData(
            "Carnation", getString(com.ysc3237.snapcat.R.string.description_flower_carnation),
            com.ysc3237.snapcat.R.drawable.cat2
        )
        mCatList.add(mCatData)
        mCatData = CatData(
            "Tulip", getString(com.ysc3237.snapcat.R.string.description_flower_tulip),
            com.ysc3237.snapcat.R.drawable.cat3
        )
        mCatList.add(mCatData)
        mCatData = CatData(
            "Daisy", getString(com.ysc3237.snapcat.R.string.description_flower_daisy),
            com.ysc3237.snapcat.R.drawable.cat4
        )
        mCatList.add(mCatData)
        mCatData = CatData(
            "Sunflower", getString(com.ysc3237.snapcat.R.string.description_flower_sunflower),
            com.ysc3237.snapcat.R.drawable.cat5
        )
        mCatList.add(mCatData)
        mCatData = CatData(
            "Daffodil", getString(com.ysc3237.snapcat.R.string.description_flower_daffodil),
            com.ysc3237.snapcat.R.drawable.cat6
        )
        mCatList.add(mCatData)
        mCatData = CatData(
            "Gerbera", getString(com.ysc3237.snapcat.R.string.description_flower_gerbera),
            com.ysc3237.snapcat.R.drawable.cat7
        )
        mCatList.add(mCatData)
        mCatData = CatData(
            "Orchid", getString(com.ysc3237.snapcat.R.string.description_flower_orchid),
            com.ysc3237.snapcat.R.drawable.cat8
        )
        mCatList.add(mCatData)
        mCatData = CatData(
            "Iris", getString(com.ysc3237.snapcat.R.string.description_flower_iris),
            com.ysc3237.snapcat.R.drawable.cat9
        )
        mCatList.add(mCatData)
        mCatData = CatData(
            "Lilac", getString(com.ysc3237.snapcat.R.string.description_flower_lilac),
            com.ysc3237.snapcat.R.drawable.cat1
        )
        mCatList.add(mCatData)

        val myAdapter1: myAdapter
        myAdapter1 = myAdapter(context, mCatList)
//        MyAdapter(Context mContext, List< CatData > mCatList) {
        mRecyclerView.adapter = myAdapter1

        return root
    }

}