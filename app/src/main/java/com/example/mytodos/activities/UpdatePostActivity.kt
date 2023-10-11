package com.example.mytodos.activities


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.mytodos.R
import com.example.mytodos.databinding.ActivityUpdatePostBinding
import com.example.mytodos.db.TravelPostDao
import com.example.mytodos.db.TravelPostDatabase
import com.example.mytodos.entity.EntityPost
import com.example.mytodos.repository.TravelPostRepository
import com.example.mytodos.viewmodel.TravelPostViewModel
import com.example.mytodos.viewmodel.TravelPostViewModelFactory
import java.io.File
import java.io.FileOutputStream

class UpdatePostActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUpdatePostBinding
    private lateinit var travelPostViewModel: TravelPostViewModel
    private lateinit var travelPostDatabase: TravelPostDatabase
    private lateinit var username:String
    private lateinit var password:String
    private lateinit var travelPostRepository: TravelPostRepository
    private lateinit var travelPostDao: TravelPostDao
    private lateinit var postTitle:String
    private lateinit var postLocation:String

    private lateinit var btn:String
    private var id:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        travelPostDatabase = TravelPostDatabase.getDatabase(this)
        travelPostDao = TravelPostDatabase.getDatabase(this).travelpostDAO()
        travelPostRepository = TravelPostRepository(travelPostDao)
        travelPostViewModel = ViewModelProvider(this, TravelPostViewModelFactory(travelPostRepository))[TravelPostViewModel::class.java]


        binding.imagePost.setImageResource(R.drawable.travelimg3)

        val intent = intent     //getIntent()
        username = intent.getStringExtra("USERNAME").toString()
        password = intent.getStringExtra("PASSWORD").toString()
        id=intent.getIntExtra("ID",0)
        postTitle = intent.getStringExtra("POSTTITLE").toString()
        postLocation = intent.getStringExtra("LOCATION").toString()
        btn = intent.getStringExtra("BUTTON_TEXT").toString()
        val imageUri = intent.getStringExtra("IMAGEUri")

        val img = base64ToBitmap(imageUri)
        binding.postTitle.setText(postTitle)
        binding.postLocation.setText(postLocation)

        binding.imagePost.setImageBitmap(img)

        binding.updateButton.setOnClickListener {
            postTitle = binding.postTitle.text.toString()
            postLocation = binding.postLocation.text.toString()


            val entityPost = EntityPost(id,postTitle,postLocation,username,password,imageUri)

            travelPostViewModel.updateTravelPost(entityPost)
            Toast.makeText(this, "Updated the Post", Toast.LENGTH_LONG).show()
            val iNext= Intent(this, MainActivity::class.java)
            startActivity(iNext)
        }

        binding.deleteButton.setOnClickListener {
            postTitle = binding.postTitle.text.toString()
            postLocation = binding.postLocation.text.toString()

            val entityPost = EntityPost(id,postTitle,postLocation,username,password,imageUri)

            travelPostViewModel.deleteTravelPost(entityPost)
            Toast.makeText(this, "Post Deleted", Toast.LENGTH_LONG).show()
            val iNext= Intent(this, MainActivity::class.java)
            startActivity(iNext)
        }




        binding.sendButton.setOnClickListener {
            sendimage()
        }
    }
    fun base64ToBitmap(base64String: String?): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun sendimage() {
        // Get the Drawable from the ImageView
        val drawable: Drawable? = binding.imagePost.drawable

        // Check if the ImageView contains a valid Drawable
        if (drawable is BitmapDrawable) {
            // Convert the Drawable to a Bitmap
            val bitmap: Bitmap = drawable.bitmap

            // Save the Bitmap to a temporary file
            val tempImageFile = File(cacheDir, "temp_image.jpg")

            try {
                val outputStream = FileOutputStream(tempImageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                // Create a content URI using FileProvider
                val imageUri = FileProvider.getUriForFile(
                    this,
                    "com.example.mytodos.fileprovider", // Use the same authority as defined in AndroidManifest.xml
                    tempImageFile
                )

                // Create an intent to share the image
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "image/jpeg"
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)

                // Grant read permission to the receiving app
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                // Check if there are apps that can handle the share intent
                val packageManager = packageManager
                if (shareIntent.resolveActivity(packageManager) != null) {
                    startActivity(Intent.createChooser(shareIntent, "Share Image"))
                } else {
                    // Handle the case where there are no apps to handle the share intent
                    showToast("No apps available to share the image.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Error preparing image for sharing.")
            }
        } else {
            showToast("No image in ImageView to share.")
        }

    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}