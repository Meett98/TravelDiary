package com.example.mytodos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.mytodos.db.TravelPostDao
import com.example.mytodos.db.TravelPostDatabase
import com.example.mytodos.entity.EntityPost
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TravelDiaryRoomTest {
    lateinit var travelPostDatabase: TravelPostDatabase
    lateinit var travelPostDao: TravelPostDao
    @Before
    fun setUp() {
        travelPostDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TravelPostDatabase::class.java
        ).allowMainThreadQueries().build()

        travelPostDao = travelPostDatabase.travelpostDAO()
    }


    @Test
    fun testInsertAndRetrieveTravelPost() = runBlocking {
        // Insert a test post into the in-memory database
        val testPost = EntityPost(1,"Mumbai","Maharastra", "newUser","1234",R.drawable.travelimg1.toString())
        travelPostDao.insertTravelPost(testPost)

        // Retrieve the post and assert it matches the inserted post
        val result = travelPostDao.getAllTravelPost("newUser")
        assertEquals(1, result.size)
        assertEquals(testPost, result[0])
    }

    @Test
    fun testDeleteTravelPost() = runBlocking {
        // Insert a test post into the in-memory database
        val testPost = EntityPost(1,"Mumbai","Maharastra", "newUser","1234",R.drawable.travelimg1.toString())
        travelPostDao.insertTravelPost(testPost)

        // Delete the test post
        travelPostDao.deleteTravelPost(testPost)

        // Retrieve the posts and assert that it's empty
        val result = travelPostDao.getAllTravelPost("newUser")
        assertTrue(result.isEmpty())
    }


    @Test
    fun testUpdateTravelPost() = runBlocking {
        // Insert a test post into the in-memory database
        val testPost = EntityPost(1,"Mumbai","Maharastra", "newUser","1234",R.drawable.travelimg1.toString())
        travelPostDao.insertTravelPost(testPost)

        // Modify the test post and update it
        val updatedPost = testPost.copy(/* updated fields */)
        travelPostDao.updateTravelPost(updatedPost)

        // Retrieve the updated post and assert it matches the modified post
        val result = travelPostDao.getAllTravelPost("newUser")
        assertEquals(1, result.size)
        assertEquals(updatedPost, result[0])
    }

    @After
    fun tearDown() {
        travelPostDatabase.close()
    }
}