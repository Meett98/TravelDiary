package com.example.mytodos.activities

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mytodos.R
import com.example.mytodos.databinding.ActivityDownloadBinding

class DownloadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDownloadBinding
    private val handler = Handler(Looper.getMainLooper())
    private var progress = 0
    private val maxProgress = 100 // Set the maximum progress value here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)


            simulateDownloadCompletion()





    }

    private fun simulateDownloadCompletion() {
        val updateInterval = 30L // Update the progress every 30 milliseconds
        val totalDuration = 3000L // Total duration for the animation (3 seconds)

        val numSteps = (totalDuration / updateInterval).toInt()
        val stepSize = maxProgress.toFloat() / numSteps

        val updateRunnable = object : Runnable {
            override fun run() {
                if (progress < maxProgress) {
                    progress += stepSize.toInt()
                    binding.progressBar.progress = progress


                    val percentage = (progress.toFloat() / maxProgress * 100).toInt()
                    binding.percent.text = "$percentage %"
                    handler.postDelayed(this, updateInterval)
                } else {
                    // Animation complete
                    binding.progressBar.progress = maxProgress

                    binding.download.text = "    Completed."
                    // Call a function to handle the download completion
                    handleDownloadComplete()
                }
            }
        }

        handler.post(updateRunnable)

    }
    private fun handleDownloadComplete() {
        // Implement code to handle the download completion, such as showing a notification
        Toast.makeText(this,"Download Completed",Toast.LENGTH_SHORT).show()
        createNotification()
    }


    private fun createNotification() {
        // Create a notification channel (for Android 8.0 and higher)
        createNotificationChannel()




        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Download Completed")
            .setContentText("Your file has been downloaded.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@DownloadActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(NOTIFICATION_ID, notification.build())
        }





    }

    private fun createNotificationChannel() {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {

                val name = "Download Channel"
                val descriptionText = "Channel for download notifications"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }

                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

    }

    companion object {
        private const val CHANNEL_ID = "download_channel"
        private const val NOTIFICATION_ID = 1
    }

}