package com.example.mytodos.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mytodos.entity.Entity
import com.example.mytodos.entity.EntityPost

@Dao
interface TravelPostDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTravelPost(entityPost: EntityPost)

    @Update
    suspend fun updateTravelPost(entityPost: EntityPost)

    @Delete
    suspend fun deleteTravelPost(entityPost: EntityPost)

    @Query("SELECT * FROM Travel_Diary WHERE username = :username ORDER BY id ASC")
    suspend fun getAllTravelPost(username: String): List<EntityPost>

    @Query("SELECT COUNT(*) FROM Travel_Diary WHERE username = :username")
    fun getPostCount(username : String): LiveData<Int>
}