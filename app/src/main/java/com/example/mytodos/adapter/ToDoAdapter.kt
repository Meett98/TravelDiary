package com.example.mytodos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodos.databinding.TodoItemViewBinding
import com.example.mytodos.entity.Entity

class TodoAdapter(private val itodoclick : IToDoClick,
                  private val itododelete: IToDoDelete
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder> () {

    class TodoViewHolder(val binding: TodoItemViewBinding) : RecyclerView.ViewHolder(binding.root)


    private var todos=ArrayList<Entity>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(TodoItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currTodo = todos[position]
        holder.binding.titleTask.text=currTodo.title


        holder.itemView.setOnClickListener {
            itodoclick.onItemClick(todos.get(position))
        }

        holder.binding.btnDel.setOnClickListener {
            itododelete.onDeleteClick(todos.get(position))
        }

    }

    override fun getItemCount(): Int {
        return todos.size;
    }

    fun updateTodoList(updatedTodo: List<Entity>)
    {
        todos.clear()
        todos.addAll(updatedTodo)
        notifyDataSetChanged()
    }
}


interface IToDoDelete {
    fun onDeleteClick(entity: Entity)
}

interface IToDoClick {
    fun onItemClick(entity: Entity)
}