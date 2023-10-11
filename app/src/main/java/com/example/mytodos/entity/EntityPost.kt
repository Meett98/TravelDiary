package com.example.mytodos.entity

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Travel_Diary")
data class EntityPost(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var posttitle:String,
    var location:String,
    var username : String,
    var password: String,
//    val imageResourceId: Int?, // Nullable Int for drawable resource ID
//    val imageUri: String? // Nullable String for image URI
    val imageUri:String?

)