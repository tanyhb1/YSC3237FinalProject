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
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import android.graphics.drawable.Drawable
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import com.google.android.gms.maps.model.*
import com.ysc3237.snapcat.MainActivity
import com.ysc3237.snapcat.ui.Catsfeed.DetailActivity


/**
 * Class for home fragment.
 * Displays singapore map with clickable markers of cat photos.
 * When clicked, markers display caption.
 * Displays user location.
 * @since 1.0
 */

class HomeFragment : Fragment() {
    private var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

    /**
     * Initializes map.
     * zooms and centers it at Singapore.
     * creates clickable markers for cats.
     */
    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
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
            MainActivity.catList.forEach {
                googleMap!!.addMarker(MarkerOptions()
                    .position(it.catLocation)
                    .title(it.catName)
                    .snippet(it.catDescription)
                    .icon(bitmapDescriptorFromVector(context!!, R.drawable.cat_map_icon_24dp)))
            }

            mMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker): Boolean {
                    val mIntent = Intent(context!!, DetailActivity::class.java)
                    val cat = MainActivity.catList.filter { cat -> cat.catName == marker.title }.get(0)


                    mIntent.putExtra("Title", cat.catName)
                    mIntent.putExtra("Description", cat.catDescription)
                    val catB = cat.catBitmap
                    if (catB != null) {
                        mIntent.putExtra("Image", -1)
                        mIntent.putExtra("Bitmap", catB)
                    } else
                        mIntent.putExtra("Image", cat.catImage)

                    context!!.startActivity(mIntent);
                    return true
                }
            })

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

    /**
     * Creates a bitmap descriptor from vector.
     * Used to make icons from cat photo.
     */
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







