package com.example.mytodos.activities

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mytodos.R
import com.example.mytodos.databinding.ActivityCreatePostBinding
import com.example.mytodos.db.TravelPostDao
import com.example.mytodos.db.TravelPostDatabase
import com.example.mytodos.entity.EntityPost
import com.example.mytodos.entity.TravelEntityRDB
import com.example.mytodos.repository.TravelPostRepository
import com.example.mytodos.viewmodel.TravelPostViewModel
import com.example.mytodos.viewmodel.TravelPostViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.io.IOException

class CreatePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePostBinding
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var postTitle: String
    private lateinit var postLocation: String
    private var id: Int = 0
    private var selectedImageUri: Uri? = null
    private lateinit var dbRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent     //getIntent()
        username = intent.getStringExtra("USERNAME").toString()
        password = intent.getStringExtra("PASSWORD").toString()

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        dbRef = FirebaseDatabase.getInstance().getReference("TravelRDB/$uid")



        binding.postButton.setOnClickListener {
            postTitle = binding.postTitle.text.toString()
            postLocation = binding.postLocation.text.toString()
            var imageUri: String? = null






            if (selectedImageUri != null) {


                val image = uriToBitmap(this, selectedImageUri!!)
                val resize = resizeImage(image, 380, 200)
                if (resize != null) {
                    imageUri = bitmapToBase64(resize)
                }
            }
            if (postTitle == "") {
                Toast.makeText(this, "Please Enter the Post Title", Toast.LENGTH_SHORT).show()
            } else if (selectedImageUri == null) {
                Toast.makeText(this, "Please Select the image from gallery", Toast.LENGTH_SHORT)
                    .show()
            } else {

                val childId = dbRef.push().key!!
                val entityRDB = TravelEntityRDB(childId, postTitle, postLocation, username, password, imageUri)
                dbRef.child(childId).setValue(entityRDB)
                    .addOnCompleteListener{
                        Toast.makeText(this, "Post Added", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error : ${it.message}", Toast.LENGTH_LONG).show()
                    }



                val iNext = Intent(this, MainActivity::class.java)
                startActivity(iNext)
            }


        }


        binding.btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"

            startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT)

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT
            && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {

            selectedImageUri = data.data!!
            binding.imagePost.setImageURI(selectedImageUri)
        }

    }




    // Use the URI to load the image as a Bitmap
    fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun resizeImage(originalBitmap: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {

        if (originalBitmap == null)
            return null
        // Create a new Bitmap with the desired dimensions
        val resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)

        // Create a Canvas to draw the original image onto the new Bitmap
        val canvas = Canvas(resizedBitmap)

        // Calculate scaling factors for width and height
        val scaleX = newWidth.toFloat() / originalBitmap.width
        val scaleY = newHeight.toFloat() / originalBitmap.height

        // Set the scaling factors and draw the original image
        canvas.scale(scaleX, scaleY)
        canvas.drawBitmap(originalBitmap, 0f, 0f, null)


        return resizedBitmap
    }

    companion object {
        private const val CHANNEL_ID = "download_channel"
        private const val NOTIFICATION_ID = 1
        const val REQUEST_CODE_OPEN_DOCUMENT = 12345
    }
}