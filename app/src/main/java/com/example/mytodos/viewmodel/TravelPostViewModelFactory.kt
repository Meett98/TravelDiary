package com.example.mytodos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytodos.repository.ToDoRepository
import com.example.mytodos.repository.TravelPostRepository

class TravelPostViewModelFactory (private val travelPostRepository: TravelPostRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        return TravelPostViewModel(travelPostRepository) as T
    }

}