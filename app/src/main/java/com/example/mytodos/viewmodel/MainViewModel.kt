package com.example.mytodos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodos.repository.ToDoRepository
import com.example.mytodos.entity.Entity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class MainViewModel(private val todoDatabase: TodoDatabase):ViewModel() {
class MainViewModel(private val toDoRepository: ToDoRepository):ViewModel() {
    private val todosmutable = MutableLiveData<List<Entity>>()

    val alltodos:LiveData<List<Entity>>
        get() = todosmutable

    fun insertTodo(entity: Entity) = viewModelScope.launch(Dispatchers.IO){
        toDoRepository.insertToDoRepo(entity)
    }

    fun updateTodo(entity: Entity) = viewModelScope.launch(Dispatchers.IO){
        toDoRepository.updateToDoRepo(entity)
        }


    fun deleteTodo(entity: Entity)= viewModelScope.launch(Dispatchers.IO) {
        toDoRepository.deleteToDoRepo(entity)
        }


    fun getAllTodos(username:String) = viewModelScope.launch(Dispatchers.IO){
            var result:List<Entity> = toDoRepository.getallToDoRepo(username)
            todosmutable.postValue(result)
        }


}