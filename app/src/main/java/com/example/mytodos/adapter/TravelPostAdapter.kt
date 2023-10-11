package com.example.mytodos.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodos.R
import com.example.mytodos.databinding.ItemPostBinding
import com.example.mytodos.entity.EntityPost
import java.lang.Exception

class TravelPostAdapter(private val itravelpostclick : ITravelPostClick) : RecyclerView.Adapter<TravelPostAdapter.TravelPostViewHolder>() {

    class TravelPostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    private var travelPosts=ArrayList<EntityPost>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelPostViewHolder {
        return TravelPostAdapter.TravelPostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: TravelPostViewHolder, position: Int) {
        val currTodo = travelPosts[position]
        holder.binding.postTitle.text=currTodo.posttitle
        if(currTodo.location != "") {
            holder.binding.postLocation.text = "Location : " + currTodo.location
        }


        try {
            val str = currTodo.imageUri
            val img = base64ToBitmap(str)
            Log.d("BitmapImg",img.toString())
            holder.binding.imagePost.setImageBitmap(img)
        }
        catch (e:Exception)
        {
            holder.binding.imagePost.setImageResource(R.drawable.ic_launcher_foreground)

        }
        holder.itemView.setOnClickListener {
            itravelpostclick.onPostItemClick(travelPosts[position])
        }
        Log.d("AddedPost","Success")

    }

    override fun getItemCount(): Int {
        return travelPosts.size
    }

    fun updateTravelPostList(updatedTravelPost: List<EntityPost>)
    {
        travelPosts.clear()
        travelPosts.addAll(updatedTravelPost)
        notifyDataSetChanged()
    }
    // Decode a base64 string back to a Bitmap
    fun base64ToBitmap(base64String: String?): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }


}

interface ITravelPostClick {
    fun onPostItemClick(entityPost: EntityPost)
}
