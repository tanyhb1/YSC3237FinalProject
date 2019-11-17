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

/**
 * Fills the Catsfeed fragment with cardviews of catphotos along with their names.
 * Places each of the cardview in a recycler view and uses a grid layout manager.
 * Initializes list of dummy cat data (photos - names - descriptions).
 * Declares and initialize Recyclerview and the actual data to be displayed in Android Gridlayout.
 * @see myAdapter
 * @see CatViewHolder
 */

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
        mRecyclerView.adapter = myAdapter1

        return root
    }

}