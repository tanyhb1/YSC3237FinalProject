package com.ysc3237.snapcat.ui.Catsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ysc3237.snapcat.R

class CatsfeedFragment : Fragment() {

    private lateinit var catsfeedViewModel: CatsfeedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        catsfeedViewModel =
            ViewModelProviders.of(this).get(CatsfeedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_catsfeed, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        catsfeedViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

}