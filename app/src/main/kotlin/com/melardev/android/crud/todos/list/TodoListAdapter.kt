package com.melardev.android.crud.todos.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.melardev.android.crud.databinding.TodoListItemBinding
import com.melardev.android.crud.datasource.common.entities.Todo
import java.util.*

class TodoListAdapter(private val todoRowEventListener: TodoRowEventListener) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private val todos: ArrayList<Todo> = ArrayList()

    interface TodoRowEventListener {
        fun onClicked(todo: Todo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = TodoListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val todo = todos[position]
        viewHolder.binding.txtId.text = todo.id.toString()
        viewHolder.binding.txtTitle.text = todo.title
        viewHolder.binding.txtDescription.text = todo.description
        viewHolder.binding.checkboxCompleted.isChecked = todo.completed
        viewHolder.binding.txtCreatedAt.text = todo.createdAt
        viewHolder.binding.txtUpdatedAt.text = todo.updatedAt


        viewHolder.itemView.setOnClickListener { todoRowEventListener.onClicked(todo) }

    }

    override fun getItemCount(): Int {
        return todos.size
    }

    fun setItems(todos: List<Todo>?) {
        if (todos == null) {
            this.todos.clear()
            notifyDataSetChanged()
            return
        }

        val startPosition = this.todos.size
        this.todos.addAll(todos)
        notifyItemRangeChanged(startPosition, todos.size)
    }

    fun getItem(position: Int): Todo {
        return todos[position]
    }

    inner class ViewHolder(var binding: TodoListItemBinding) : RecyclerView.ViewHolder(binding.getRoot())
}
