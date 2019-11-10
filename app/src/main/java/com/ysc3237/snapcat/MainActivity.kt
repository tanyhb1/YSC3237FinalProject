package com.ysc3237.snapcat

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.core.app.ActivityCompat.startActivityForResult
import android.provider.MediaStore
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import com.google.android.gms.ads.internal.gmsg.HttpClient
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        Log.d("HELLO", " WORLD")

        // Get persistent location manager to give location data for photos and map
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // resume normal
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

    lateinit var imageView: ImageView
    lateinit var captureButton: Button

    val REQUEST_IMAGE_CAPTURE = 1


    private val PERMISSION_REQUEST_CODE: Int = 101

    private var mCurrentPhotoPath: String? = null;
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

                // Get location info:
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
                                        // Log.d("SNAPCAT", "Uploading... "+bytesUploaded+" of "+totalBytes+" bytes");
                                    }
                                })
                                .getAsJSONObject(object: JSONObjectRequestListener {
                                    override fun onResponse(response: JSONObject) {
                                        Log.d("SNAPCAT", "Got response "+response);
                                    }
                                    override fun onError(error: ANError) {
                                        Log.d("SNAPCAT", "Got error "+error);

                                    }
                                })
                            Log.d("SNAPCAT", "Uploaded");
                        }
                    }
                // Uploading image etc:
                Log.d("SNAPCAT", "Uploading file - "+mCurrentPhotoPath);

            }
        }
    }



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
