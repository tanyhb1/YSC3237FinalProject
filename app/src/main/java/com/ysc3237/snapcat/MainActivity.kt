package com.ysc3237.snapcat

import android.Manifest.permission.*
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import android.provider.MediaStore
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import com.google.android.gms.maps.model.LatLng
import com.ysc3237.snapcat.ui.Catsfeed.CatData
import com.ysc3237.snapcat.ui.Catsfeed.CatsfeedFragment
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


/**
 * Main activity that acts as a controller for our app's functionality.
 * Loads our photos from the server on the feed, and provides photo-taking functionality together with interaction with our server.
 * @author Bryan Tan, Haroun Chahed, Hebe Hilhorst
 * @since 1.0
 */

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        val catList = ArrayList<CatData>()
        val serverURL: String = "http://hebehh.pythonanywhere.com"
        lateinit var instance: MainActivity
        var catListLoaded = false;
    }

    /**
     * Initialize our catList.
     */
    fun initCats() {
        if(!catListLoaded)
            loadCats()
    }

    /**
     * Load cats.
     */
    fun loadCats() {
        catList.clear()

        AndroidNetworking.get(serverURL+"/list_cats")
            .build()
            .getAsJSONArray(object: JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    Log.d("SNAPCAT", "Got "+response?.length()+" cats. response for loadcats - "+response)

                    for (i in 0 until response!!.length()) {
                        val cat = response?.getJSONObject(i)

                        Log.d("SNAPCAT", "Getting image from "+serverURL+"/Images/"+cat!!.get("fileName"))
                        AndroidNetworking.get(serverURL+"/Images/"+cat!!.get("fileName"))
                            .setBitmapConfig(Bitmap.Config.ARGB_8888)
                            .build()
                            .getAsBitmap(object: BitmapRequestListener {
                                override fun onResponse(bitmap: Bitmap) {
                                    Log.d("SNAPCAT", "got bitmap for element "+i+" of size "+bitmap.width+" and "+bitmap.height)

                                    val newCat = CatData(
                                        i.toString(),
                                        cat.get("caption") as String,
                                        R.drawable.cat1,
                                        bitmap,
                                        LatLng(cat.get("latitude") as Double, cat.get("longitude") as Double)
                                    )

                                    Log.d("SNAPCAT", "Adding cat "+newCat.catName+", number "+i+", caption "+newCat.catDescription)
                                    catList.add(newCat)

                                    CatsfeedFragment.myAdapter1.notifyDataSetChanged()
                                }

                                override fun onError(error: ANError) {
                                    Log.d("SNAPCAT", "Error getting bitmap for element "+i+" - "+error?.message)
                                }
                            })
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("SNAPCAT", "Got error for loadcats - "+anError?.message)
                }
            })

        loadDummyData()
        catListLoaded = true
    }

    /**
     * Generates dummy coordinates for testing.
     * @see: loadDummyData
     */
    private fun getDummyLatLng(): LatLng {
        val latMax = 1.437690
        val latMin = 1.315238
        val lngMax = 104.014576
        val lngMin = 103.638152
        val divs = 100

        val lat = latMin+(((0..divs).random()).toDouble()/divs)*(latMax-latMin)
        val lng = lngMin+(((0..divs).random()).toDouble()/divs)*(lngMax-lngMin)

        return LatLng(lat, lng)
    }

    private fun loadDummyData() {
        catList.add(CatData(
            "Rose", getString(com.ysc3237.snapcat.R.string.description_cat_rose),
            com.ysc3237.snapcat.R.drawable.cat1,
            null,
            getDummyLatLng()
        ));
        catList.add(CatData(
            "Carnation", getString(com.ysc3237.snapcat.R.string.description_cat_carnation),
            com.ysc3237.snapcat.R.drawable.cat2,
            null,
            getDummyLatLng()
        ))
        catList.add(CatData(
            "Tulip", getString(com.ysc3237.snapcat.R.string.description_cat_tulip),
            com.ysc3237.snapcat.R.drawable.cat3,
            null,
            getDummyLatLng()
        ))
        catList.add(CatData(
            "Daisy", getString(com.ysc3237.snapcat.R.string.description_cat_daisy),
            com.ysc3237.snapcat.R.drawable.cat4,
            null,
            getDummyLatLng()
        ))
        catList.add(CatData(
            "Sunflower", getString(com.ysc3237.snapcat.R.string.description_cat_sunflower),
            com.ysc3237.snapcat.R.drawable.cat5,
            null,
            getDummyLatLng()
        ))
        catList.add(CatData(
            "Daffodil", getString(com.ysc3237.snapcat.R.string.description_cat_daffodil),
            com.ysc3237.snapcat.R.drawable.cat6,
            null,
            getDummyLatLng()
        ))
        catList.add(CatData(
            "Gerbera", getString(com.ysc3237.snapcat.R.string.description_cat_gerbera),
            com.ysc3237.snapcat.R.drawable.cat7,
            null,
            getDummyLatLng()
        ))
        catList.add(CatData(
            "Orchid", getString(com.ysc3237.snapcat.R.string.description_cat_orchid),
            com.ysc3237.snapcat.R.drawable.cat8,
            null,
            getDummyLatLng()
        ))
        catList.add(CatData(
            "Iris", getString(com.ysc3237.snapcat.R.string.description_cat_iris),
            com.ysc3237.snapcat.R.drawable.cat9,
            null,
            getDummyLatLng()
        ))
        catList.add(CatData(
            "Lilac", getString(com.ysc3237.snapcat.R.string.description_cat_lilac),
            com.ysc3237.snapcat.R.drawable.cat1,
            null,
            getDummyLatLng()
        ))
    }

    /**
     * Creates our list of cat photos accessed from the server. Provides interaction between our server and the camera.
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        instance = this
        initCats()


        // Get persistent location manager to give location data for photos and map
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_catsfeed , R.id.navigation_home, R.id.navigation_profile
            )
        )
        captureButton = findViewById(R.id.btn_capture)
        captureButton.setOnClickListener (View.OnClickListener {
            Log.d("SNAPCAT", "Button clicked")
            if (checkPermission()) {
                Log.d("SNAPCAT", "Taking picture...")
                takePicture()
            } else {
                Log.d("SNAPCAT", "Requesting permission...")
                requestPermission()
            }
        })
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        AndroidNetworking.initialize(getApplicationContext());
    }

    lateinit var captureButton: Button
    val REQUEST_IMAGE_CAPTURE = 1
    private val PERMISSION_REQUEST_CODE: Int = 101
    private var mCurrentPhotoPath: String? = null

    /**
     *  Requests for permissions, and returns the result.
     *  @author haefihs
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    takePicture()

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        Log.d("SNAPCAT", "The storage dir is "+storageDir);

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            mCurrentPhotoPath = absolutePath;
        }
    }

    private fun takePicture() {
        val REQUEST_IMAGE_CAPTURE = 1
        val REQUEST_TAKE_PHOTO = 1

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Log.d("SNAPCAT", "Could not create image file");
                    null
                }

                photoFile?.also {
                    Log.d("SNAPCAT", "Image file created - Photofile is "+photoFile.toString())

                    val photoURI: Uri = FileProvider.getUriForFile(
                        getApplicationContext(),
                        "com.ysc3237.snapcat.fileprovider",
                        it
                    )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }



    // TODO: Let user add caption
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            var bitmap: Bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)

            Log.d("SNAPCAT", "File loaded.");

            class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
                init {
                    execute()
                }

                override fun doInBackground(vararg params: Void?): Void? {
                    handler()
                    return null
                }
            }



            doAsync {
                Log.d("SNAPCAT","HELLO WORLD")
                val url: String = "http://hebehh.pythonanywhere.com/upload"
//                var url: String = "http://b7bf48c8.ngrok.io/upload"
                val imageFile = File(mCurrentPhotoPath)
                //TODO: Compress file

                // Get location info, then upload:
                fusedLocationClient.lastLocation
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("SNAPCAT", "completed get location successfully")

                            // Get useful location data
                            val location = task.result
                            val lati = location.latitude
                            val longi = location.longitude
                            Log.d(
                                "SNAPCAT",
                                "long: " + longi.toString() + " lat: " + lati.toString()
                            )

                            // Caption inbuilt for now
                            val capt = "Have a pretty cat."

                            // Upload with caption, latitude, and longitude
                            AndroidNetworking.upload(url)
                                .addMultipartParameter("caption", capt)
                                .addMultipartParameter("latitude", lati.toString())
                                .addMultipartParameter("longitude", longi.toString())
                                .addMultipartFile("image",imageFile)
                                .build()
                                .setUploadProgressListener(object: UploadProgressListener {
                                    override fun onProgress(bytesUploaded: Long, totalBytes: Long) {
                                         Log.d("SNAPCAT", "Uploading... "+bytesUploaded+" of "+totalBytes+" bytes");
                                    }
                                })
                                .getAsJSONObject(object: JSONObjectRequestListener {
                                    override fun onResponse(response: JSONObject) {
                                        Log.d("SNAPCAT", "Got response "+response)
                                    }
                                    override fun onError(error: ANError) {
                                        Log.d("SNAPCAT", "Got error "+error)

                                    }
                                })
                            Log.d("SNAPCAT", "Uploaded")
                            //TODO: Inform user of success
                        }
                    }
                // Uploading image etc:
                Log.d("SNAPCAT", "Uploading file - "+mCurrentPhotoPath)

            }
        }
    }

    // Check we have all necessary permissions
    // WARNING: if you need a permission, you have to add it manually. To this and to request permission
    // TODO: change to have a list of necessary permissions (maybe get direct from manifest? should be a way)
    //  and then map from that to check and request automatically so don't need to do manual
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }



    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }



}
