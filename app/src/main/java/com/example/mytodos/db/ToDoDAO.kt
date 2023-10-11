package com.example.mytodos.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mytodos.entity.Entity

@Dao
interface TodoDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTodo(entity: Entity)

    @Update
    suspend fun updateTodo(entity: Entity)

    @Delete
    suspend fun deleteTodo(entity: Entity)

    @Query("Select * from To_Do where username=:username order by id ASC ")
    fun getAllTodos(username:String):List<Entity>

}