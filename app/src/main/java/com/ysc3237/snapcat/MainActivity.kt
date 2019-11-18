package com.ysc3237.snapcat

import android.Manifest.permission.*
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import java.io.FileOutputStream
import java.io.OutputStream


/**
 * Main activity that acts as a controller for our app's functionality.
 * Loads our photos from the server on the feed, and provides photo-taking functionality together with interaction with our server.
 *
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
                                        cat.get("name") as String,
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
     * @return A random LatLng pair within the minimum Singapore-containing rectangle (some of this is in Malaysia)
     * @see: loadDummyData
     * @see CatData
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

    /**
     * Adds a selection of dummy data to the CatData
     * @return void
     * @see getDummyLatLng
     * @see CatData
     */
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
     * @param savedInstanceState
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
     * Creates a temporary file to store an image
     * @return File This is an empty file to store the new image in
     */
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
            mCurrentPhotoPath = absolutePath
        }
    }

    /**
     * Take a
     */
    private fun takePicture() {
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

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    // TODO: would be nice to keep photo up while asking for caption. but not sure how,
    //  since that's an entirely separate intent.
    // TODO: Maybe try and use callback functions instead of so much nesting?
    //  but not a major issues.
    /**
     * Handle a photo once taken: get caption and location and send to server
     * @param requestCode
     * @param resultCode
     * @param data The photo from the takePicture() intent call
     * @return Unit
     * @see CatData
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) : Unit {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            // Get and compress images
            val imageFile = File(mCurrentPhotoPath)
            var bitmap : Bitmap = BitmapFactory.Options().run {
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(mCurrentPhotoPath, this)

                // Calculate inSampleSize
                inSampleSize = calculateInSampleSize(this, 200, 200)

                // Decode bitmap with inSampleSize set
                inJustDecodeBounds = false

                BitmapFactory.decodeFile(mCurrentPhotoPath, this)
            }
            val stream : OutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()

            var capt = "This is a cat."
            var name : String
            var lati = "0"
            var longi = "0"

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
                val url: String = "http://hebehh.pythonanywhere.com/upload"

                // Get location info, then upload:
                fusedLocationClient.lastLocation
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("SNAPCAT", "completed get location successfully")

                            // Get useful location data
                            val location = task.result
                            lati = location.latitude.toString()
                            longi = location.longitude.toString()
                            Log.d(
                                "SNAPCAT",
                                "long: " + longi + " lat: " + lati
                            )

                            val builder = AlertDialog.Builder(this)
                            val inflater = layoutInflater
                            builder.setTitle("What's your caption?")
                            val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
                            val editCaption  = dialogLayout.findViewById<EditText>(R.id.editCaption)
                            val editName  = dialogLayout.findViewById<EditText>(R.id.editName)
                            builder.setView(dialogLayout)
                            builder.setPositiveButton("Done") {
                                    dialogInterface, i ->
                                capt =  editCaption.text.toString()
                                name = editName.text.toString()

                                // to make fake geographical data:
                                val ll = getDummyLatLng()
                                lati = ll.latitude.toString()
                                longi = ll.longitude.toString()

                                // Upload with caption, latitude, and longitude
                                AndroidNetworking.upload(url)
                                    .addMultipartParameter("name", name)
                                    .addMultipartParameter("caption", capt)
                                    .addMultipartParameter("latitude", lati)
                                    .addMultipartParameter("longitude", longi)
                                    .addMultipartFile("image",imageFile)
                                    .build()
                                    .setUploadProgressListener(object: UploadProgressListener {
                                        override fun onProgress(bytesUploaded: Long, totalBytes: Long) {
                                            Log.d("SNAPCAT", "Uploading... " + bytesUploaded + " of " + totalBytes + " bytes");
                                        }
                                    })
                                    .getAsJSONObject(object: JSONObjectRequestListener {
                                        override fun onResponse(response: JSONObject) {
                                            Log.d("SNAPCAT", "Got response " + response)
                                        }
                                        override fun onError(error: ANError) {
                                            Log.d("SNAPCAT", "Got error " + error)
                                        }
                                    })
                                Log.d("SNAPCAT", "Uploaded")

                                val text = "Successfully Uploaded!"
                                val duration = Toast.LENGTH_LONG
                                val toast = Toast.makeText(applicationContext, text, duration)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                val view = toast.getView()
                                view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN)
                                toast.show()
                                loadCats()
                            }
                            // Let them cancel
                            builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
                                dialog.cancel()
                            }

                            builder.show()

                        }
                    }
            }
        }
    }

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


    // TODO: change to have a list of necessary permissions (maybe get direct from manifest? should be a way)
    //  and then map from that to check and request automatically so don't need to do manual
    /**
     * Checks whether the app has READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION permissions
     * WARNING: if you need a permission, you have to add it manually. To this and to request permission
     * WARNING: doesn't request permission if not held
     * @return Boolean
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }


    /**
     * Requests READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION permissions.
     * WARNING: if you need a permission, you have to add it manually. To this and to request permission
     * @return Unit
     * @see onRequestPermissionsResult
     */
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE,
            CAMERA, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE)
    }

}
