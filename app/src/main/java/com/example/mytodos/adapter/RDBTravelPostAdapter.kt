package com.example.mytodos.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mytodos.R
import com.example.mytodos.databinding.ItemPostBinding
import com.example.mytodos.entity.EntityPost
import com.example.mytodos.entity.TravelEntityRDB
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

//class RDBTravelPostAdapter(
//    private val RDBtravelPosts: ArrayList<TravelEntityRDB>
//) : RecyclerView.Adapter<RDBTravelPostAdapter.TravelPostViewHolderRDB>() {
//
//
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelPostViewHolderRDB {
//        return RDBTravelPostAdapter.TravelPostViewHolderRDB(
//            ItemPostBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun getItemCount(): Int {
//        return RDBtravelPosts.size
//    }
//
//    override fun onBindViewHolder(holder: TravelPostViewHolderRDB, position: Int) {
//        val currTodo = RDBtravelPosts[position]
//        holder.binding.postTitle.text = currTodo.posttitle
//        if (currTodo.location != "") {
//            holder.binding.postLocation.text = "Location : " + currTodo.location
//        }
//
//
//        try {
//            val str = currTodo.imageUri
//            val img = base64ToBitmap(str)
//            Log.d("BitmapImg", img.toString())
//            holder.binding.imagePost.setImageBitmap(img)
//        } catch (e: Exception) {
//            holder.binding.imagePost.setImageResource(R.drawable.ic_launcher_foreground)
//
//        }
//
//    }
//
//    fun base64ToBitmap(base64String: String?): Bitmap {
//        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
//        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//    }
//
//    class TravelPostViewHolderRDB(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)
//
//}


class RDBTravelPostAdapter(
    private val RDBtravelPosts: ArrayList<TravelEntityRDB>
) : RecyclerView.Adapter<RDBTravelPostAdapter.TravelPostViewHolderRDB>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelPostViewHolderRDB {
        return TravelPostViewHolderRDB(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return RDBtravelPosts.size
    }

    override fun onBindViewHolder(holder: TravelPostViewHolderRDB, position: Int) {
        val currTodo = RDBtravelPosts[position]
        holder.binding.postTitle.text = currTodo.posttitle

        if (currTodo.location != "") {
            holder.binding.postLocation.text = "Location : " + currTodo.location
        }

        try {
            val str = currTodo.imageUri
            val img = base64ToBitmap(str)
            Log.d("BitmapImg", img.toString())
            holder.binding.imagePost.setImageBitmap(img)
        } catch (e: Exception) {
            holder.binding.imagePost.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }

    fun base64ToBitmap(base64String: String?): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    class TravelPostViewHolderRDB(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)
}
