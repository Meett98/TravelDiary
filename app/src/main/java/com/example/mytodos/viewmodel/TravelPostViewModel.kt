package com.example.mytodos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodos.entity.EntityPost
import com.example.mytodos.repository.TravelPostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TravelPostViewModel(private val travelPostRepository: TravelPostRepository): ViewModel() {
    private val travelpostsmutable = MutableLiveData<List<EntityPost>>()

    val allTravelPost: LiveData<List<EntityPost>>
        get() = travelpostsmutable




    private var userPostCountLiveData: LiveData<Int>? = null

    // Function to get the user's post count
    fun getUserPostCount(userId: String): LiveData<Int> {
        if (userPostCountLiveData == null) {
            userPostCountLiveData = travelPostRepository.getUserPostCountLiveData(userId)
        }
        return userPostCountLiveData!!
    }


    fun insertTravelPost(entityPost: EntityPost) = viewModelScope.launch(Dispatchers.IO){
        travelPostRepository.insertTravelPost(entityPost)
    }

    fun updateTravelPost(entityPost: EntityPost) = viewModelScope.launch(Dispatchers.IO){
        travelPostRepository.updateTravelPost(entityPost)
    }


    fun deleteTravelPost(entityPost: EntityPost)= viewModelScope.launch(Dispatchers.IO) {
        travelPostRepository.deleteTravelPost(entityPost)
    }


    fun getAllTravelPost(username:String) = viewModelScope.launch(Dispatchers.IO){
        var result:List<EntityPost> = travelPostRepository.getallTravelPost(username)
        travelpostsmutable.postValue(result)
    }




}