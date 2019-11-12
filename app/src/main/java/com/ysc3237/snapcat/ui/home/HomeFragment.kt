package com.ysc3237.snapcat.ui.home

import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.pm.PackageManager
import android.os.Bundle
//import android.support.v4.app.ActivityCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ysc3237.snapcat.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.model.BitmapDescriptor
import android.content.Context
import android.graphics.Canvas
import com.ysc3237.snapcat.MainActivity


class HomeFragment : Fragment() {
    private var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.ysc3237.snapcat.R.layout.fragment_home, null, false)
        mMapView = view.findViewById(com.ysc3237.snapcat.R.id.mapView)
        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume()
        try {
            MapsInitializer.initialize(getActivity()?.getApplicationContext())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mMapView!!.getMapAsync { mMap ->
            googleMap = mMap
//            googleMap!!.isMyLocationEnabled = true

            //To add cat marker
//            val cat1 = LatLng(1.3048, 103.8318)
//            googleMap!!.addMarker(MarkerOptions()
//                .position(cat1)
//                .title("Cat1")
//                .snippet("Caption associated with the photo should go here")
//                .icon(bitmapDescriptorFromVector(context!!, R.drawable.cat_map_icon_24dp)));
//

            val cat1 = LatLng(1.3048, 103.8318)
            MainActivity.catList.forEach {
                googleMap!!.addMarker(MarkerOptions()
                    .position(it.catLocation)
                    .title(it.catName)
                    .snippet(it.catDescription)
                    .icon(bitmapDescriptorFromVector(context!!, R.drawable.cat_map_icon_24dp)));
            }

            // For zooming functionality
            val cameraPosition = CameraPosition.Builder().target(LatLng(1.3048, 103.8318)).zoom(10f).build() // camera centered at singapore for now - must change to user location
            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
        return view
    }


    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}







