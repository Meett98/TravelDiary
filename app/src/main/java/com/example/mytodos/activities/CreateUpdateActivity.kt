package com.example.mytodos.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mytodos.databinding.ActivityCreateUpdateBinding
import com.example.mytodos.db.TodoDAO
import com.example.mytodos.db.TodoDatabase
import com.example.mytodos.entity.Entity
import com.example.mytodos.repository.ToDoRepository
import com.example.mytodos.viewmodel.MainViewModel
import com.example.mytodos.viewmodel.MainViewModelFactory

class CreateUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateUpdateBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var database: TodoDatabase
    private lateinit var username:String
    private lateinit var password:String
    private lateinit var toDoRepository: ToDoRepository
    private lateinit var todoDAO: TodoDAO
    private lateinit var todoTitle:String
    private lateinit var btn:String
    private var id:Int=0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database= TodoDatabase.getDatabase(this)
        todoDAO = TodoDatabase.getDatabase(this).todoDAO()
        toDoRepository = ToDoRepository(todoDAO)
        mainViewModel= ViewModelProvider(this, MainViewModelFactory(toDoRepository)).get(
            MainViewModel::class.java)




        val intent = intent     //getIntent()
        username = intent.getStringExtra("USERNAME").toString()
        password = intent.getStringExtra("PASSWORD").toString()
        id=intent.getIntExtra("ID",0)
        todoTitle = intent.getStringExtra("TITLE").toString()
        btn = intent.getStringExtra("BUTTON_TEXT").toString()





        if(btn=="SAVE"){
            binding.saveButton.text="SAVE TODO"
            binding.saveButton.setOnClickListener {
                todoTitle=binding.task.text.toString()
                val entity: Entity = Entity(0,todoTitle,username,password)
                mainViewModel.insertTodo(entity)
                Toast.makeText(this, "${entity.title} Added", Toast.LENGTH_LONG).show()
                val iNext= Intent(this, MainActivity::class.java)
                startActivity(iNext)
            }
        }
        else if(btn == "Update"){
            binding.createUpdate.text = "Update To-Do"
            binding.saveButton.text="UPDATE"
            binding.task.setText(todoTitle)

            binding.saveButton.setOnClickListener {
                todoTitle=binding.task.text.toString()
                val entity: Entity = Entity(id,todoTitle,username,password)
                mainViewModel.updateTodo(entity)
                Toast.makeText(this, "Todo Updated", Toast.LENGTH_LONG).show()
                val iNext= Intent(this, MainActivity::class.java)
                startActivity(iNext)
            }
        }

    }
}