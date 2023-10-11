package com.example.mytodos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytodos.repository.ToDoRepository

class MainViewModelFactory (private val toDoRepository: ToDoRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        return MainViewModel(toDoRepository) as T
    }

}