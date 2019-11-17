package com.ysc3237.snapcat.ui.Catsfeed

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

/**
 * Class of cat data containing name, description, image as int, image in Bitmap, and location.
 * this class will be used to assign values to the Recyclerview.
 * @since 1.0
 */

class CatData(val catName: String, val catDescription: String, val catImage: Int, val catBitmap: Bitmap?, val catLocation: LatLng)