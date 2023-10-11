package com.example.mytodos.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

    private lateinit var travelPostAdapter: TravelPostAdapter
    private lateinit var travelPostViewModel: TravelPostViewModel
    private lateinit var travelPostRepository: TravelPostRepository
    private lateinit var travelPostDao: TravelPostDao
    private lateinit var travelPostDatabase: TravelPostDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var travelList : ArrayList<TravelEntityRDB>
    private lateinit var dbRef : DatabaseReference
    private lateinit var rdbTravelPostAdapter: RDBTravelPostAdapter


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //for to-do list
//        todoAdapter= TodoAdapter(this,this)
//        database= TodoDatabase.getDatabase(this)
//        todoDAO = TodoDatabase.getDatabase(this).todoDAO()
//        toDoRepository = ToDoRepository(todoDAO)
//        mainViewModel= ViewModelProvider(this, MainViewModelFactory(toDoRepository))[MainViewModel::class.java]

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val username = currentUser?.displayName
        Log.d("UsernameFromRegister",username!!)
        binding.tvDisplay.text= "Welcome, $username"


//        travelPostAdapter = TravelPostAdapter(this)
//        travelPostDatabase = TravelPostDatabase.getDatabase(this)
//        travelPostDao = TravelPostDatabase.getDatabase(this).travelpostDAO()
//        travelPostRepository = TravelPostRepository(travelPostDao)
//        travelPostViewModel = ViewModelProvider(this,TravelPostViewModelFactory(travelPostRepository))[TravelPostViewModel::class.java]



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

        binding.btnDownload.setOnClickListener{

            val dialogFragment = ConfirmationDialogFragment()
            dialogFragment.show(supportFragmentManager, "ConfirmationDialog")
        }
        travelList = ArrayList<TravelEntityRDB>()

        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        if (password != null && username != null) {
            addPost(username,password)
        }
        getTravelDataFromRDB()






//        setLayoutManagerAndAdapterForTravelPost()
//        if (username != null) {
//            updateTravelPost(username)
//        }




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

    private fun getTravelDataFromRDB() {
        dbRef = FirebaseDatabase.getInstance().getReference("TravelRDB")
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
                                        iUpdate.putExtra("BUTTON_TEXT","Update")
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
        var count = -1
        binding.add.setOnClickListener {
            travelPostViewModel.getUserPostCount(username).observe(this) {
                val iCreate=Intent(this@MainActivity, CreatePostActivity::class.java)
                iCreate.putExtra("USERNAME",username)
                iCreate.putExtra("PASSWORD",password)
                iCreate.putExtra("ID",0)
                iCreate.putExtra("TITLE","")
                iCreate.putExtra("LOCATION","")
                iCreate.putExtra("BUTTON_TEXT","SAVE")
                iCreate.putExtra("COUNT",it)
                startActivity(iCreate)
            }

        }

    }


    override fun onDestroy() {
        super.onDestroy()
            unregisterReceiver(receiver)

    }



    private fun setLayoutManagerAndAdapterForTravelPost() {
        binding.recyclerView.apply {
            layoutManager= LinearLayoutManager(this@MainActivity)
            adapter=travelPostAdapter
        }
    }



    private fun updateTravelPost(username: String) {
        travelPostViewModel.getAllTravelPost(username)
        travelPostViewModel.allTravelPost.observe(this){travelPostlist ->
            travelPostlist?.let{
                travelPostAdapter.updateTravelPostList(it)

            }
        }
    }

//    override fun onPostItemClick(entityPost: EntityPost) {
////        val iUpdate=Intent(this@MainActivity, UpdatePostActivity::class.java)
////        iUpdate.putExtra("USERNAME",entityPost.username)
////        iUpdate.putExtra("PASSWORD",entityPost.password)
////        iUpdate.putExtra("ID",entityPost.id)
////        iUpdate.putExtra("POSTTITLE",entityPost.posttitle)
////        iUpdate.putExtra("LOCATION",entityPost.location)
////        iUpdate.putExtra("IMAGEUri",entityPost.imageUri)
////        iUpdate.putExtra("BUTTON_TEXT","Update")
////        startActivity(iUpdate)
////
////        setLayoutManagerAndAdapterForTravelPost()
////        updateTravelPost(entityPost.username)
//    }





    private fun clearSharedPreferenceData() {
        val pref: SharedPreferences = getSharedPreferences("Register_Activity", MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  pref.edit()
        editor.clear()
        editor.apply()
    }




}