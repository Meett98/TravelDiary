package com.example.mytodos.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytodos.R
import com.example.mytodos.adapter.ITravelPostClick
import com.example.mytodos.adapter.RDBTravelPostAdapter
import com.example.mytodos.adapter.TravelPostAdapter
import com.example.mytodos.broadcastreceiver.AirplaneModeChangeReceiver
import com.example.mytodos.databinding.ActivityMainBinding
import com.example.mytodos.db.TravelPostDao
import com.example.mytodos.db.TravelPostDatabase
import com.example.mytodos.entity.EntityPost
import com.example.mytodos.entity.TravelEntityRDB
import com.example.mytodos.fragment.ConfirmationDialogFragment
import com.example.mytodos.repository.TravelPostRepository
import com.example.mytodos.viewmodel.TravelPostViewModel
import com.example.mytodos.viewmodel.TravelPostViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.Exception


class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private var receiver = AirplaneModeChangeReceiver()

    private lateinit var auth: FirebaseAuth
    private lateinit var travelList : ArrayList<TravelEntityRDB>
    private lateinit var dbRef : DatabaseReference
    private lateinit var rdbTravelPostAdapter: RDBTravelPostAdapter


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uid = currentUser?.uid
        val username = currentUser?.displayName
        Log.d("UsernameFromRegister",username!!)
        binding.tvDisplay.text= "Welcome, $username"



        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)

        // Register the BroadcastReceiver with the IntentFilter
        registerReceiver(receiver, intentFilter)


        val pref=getSharedPreferences("Register_Activity", MODE_PRIVATE)
        val password= pref.getString("PASSWORD","")!!






        binding.btnLogout.setOnClickListener {
//            clearSharedPreferenceData()
//            val iPrev=Intent(this, LoginActivity::class.java)
//            startActivity(iPrev)
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


        travelList = ArrayList<TravelEntityRDB>()

        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        if (password != null && username != null) {
            addPost(username,password)
        }
        getTravelDataFromRDB(uid!!)
        dbRef = FirebaseDatabase.getInstance().getReference("TravelRDB/$uid")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val count = dataSnapshot.childrenCount.toInt()
                    val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                    val isNotificationShown = sharedPreferences.getBoolean("notificationShown", false)

                    if (count == 4 && !isNotificationShown) {
                        createNotification()
                        sharedPreferences.edit().putBoolean("notificationShown", true).apply()
                    }

                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors, if necessary
            }
        })


        FirebaseMessaging.getInstance().token
            .addOnCompleteListener {

                if(!it.isSuccessful)
                {
                    Log.e("TokenDetails","Token Failed To Receive")
                }
                val token = it.result
                Log.d("TOKEN",token)
            }

    }

    private fun getTravelDataFromRDB(uid:String) {
        dbRef = FirebaseDatabase.getInstance().getReference("TravelRDB/$uid")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(data: DataSnapshot) {
                travelList.clear()
                try {
                    if (data.exists()) {
                        for (posts in data.children) {
                            val travelData = posts.getValue(TravelEntityRDB::class.java)
                            travelList.add(travelData!!)
                        }

                        val rdbAdapter = RDBTravelPostAdapter(travelList)
                        binding.recyclerView.adapter = rdbAdapter
                        rdbAdapter.setOnItemClickListener(object : RDBTravelPostAdapter.OnItemClickListener {
                            override fun onItemClick(position: Int) {

                                        val iUpdate=Intent(this@MainActivity, UpdatePostActivity::class.java)
                                        val li = travelList[position]
                                        iUpdate.putExtra("USERNAME",li.username)
                                        iUpdate.putExtra("PASSWORD",li.password)
                                        iUpdate.putExtra("ID",li.id)
                                        iUpdate.putExtra("POSTTITLE",li.posttitle)
                                        iUpdate.putExtra("LOCATION",li.location)
                                        iUpdate.putExtra("IMAGEUri",li.imageUri)
                                        startActivity(iUpdate)
                            }
                        })

                    }
                }
                catch (e:Exception)
                {
                    Log.d("RDBSIZE",e.message.toString())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun addPost(username: String, password: String) {
        binding.add.setOnClickListener {
                val iCreate=Intent(this@MainActivity, CreatePostActivity::class.java)
                iCreate.putExtra("USERNAME",username)
                iCreate.putExtra("PASSWORD",password)
                startActivity(iCreate)
            }



    }


    override fun onDestroy() {
        super.onDestroy()
            unregisterReceiver(receiver)

    }





    private fun clearSharedPreferenceData() {
        val pref: SharedPreferences = getSharedPreferences("Register_Activity", MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  pref.edit()
        editor.clear()
        editor.apply()
    }

    private fun createNotification() {
        // Create a notification channel (for Android 8.0 and higher)
        createNotificationChannel()


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Congratulations!!")
            .setContentText("You've reached 4 posts in your travel diary.")
            .setSmallIcon(R.drawable.twotone_celebration_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(NOTIFICATION_ID, notification.build())
        }


    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

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
