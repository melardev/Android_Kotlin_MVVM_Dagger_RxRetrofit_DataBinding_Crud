package com.melardev.android.crud.todos.write

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.melardev.android.crud.R
import com.melardev.android.crud.databinding.TodoWriteBinding
import com.melardev.android.crud.datasource.common.entities.Todo
import com.melardev.android.crud.factories.AppViewModelFactory
import com.melardev.android.crud.todos.base.BaseActivity
import com.melardev.android.crud.todos.list.MainActivity
import javax.inject.Inject

class TodoCreateEditActivity : BaseActivity() {


    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory

    private var todoWriteViewModel: TodoWriteViewModel? = null
    private var editMode: Boolean = false
    private var todoId: Long = 0

    private var binding: TodoWriteBinding? = null
    private lateinit var todo: Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (intent != null) {
            this.todoId = intent.getLongExtra("TODO_ID", -1)
            this.editMode = todoId.toInt() != -1
        }
        initView()
        initViewModel()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_create_edit)
        if (editMode) {
            binding!!.btnSaveTodo.text = "Save Changes"
        } else {
            binding!!.btnSaveTodo.text = "Create"
        }
    }


    private fun initViewModel() {

        todoWriteViewModel = ViewModelProviders.of(this, appViewModelFactory)
            .get(TodoWriteViewModel::class.java)

        if (editMode) {
            // Observe the list, if
            todoWriteViewModel!!.todo.observe(this, Observer { resource ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Looper.getMainLooper().isCurrentThread)
                        throw AssertionError("")
                } else {
                    if (Looper.getMainLooper() != Looper.myLooper())
                        throw AssertionError("")
                }

                when {
                    resource.isLoading -> displayLoader()
                    resource.data != null -> {
                        todo = resource.data
                        binding!!.eTxtTitle.setText(todo.title)
                        binding!!.eTxtDescription.setText(todo.description)
                        binding!!.txtId.text = todo.id.toString()
                        binding!!.eCheckboxCompleted.isChecked = todo.completed

                    }
                    else -> handleErrorResponse(resource.fullMessages)
                }
            })

            todoWriteViewModel!!.loadTodo(todoId)
        }
    }

    fun saveTodo(view: View) {
        val title = binding!!.eTxtTitle.text.toString()
        val description = binding!!.eTxtDescription.text.toString()
        val completed = binding!!.eCheckboxCompleted.isChecked

        if (editMode) {
            todo.title = title
            todo.description = description
            todo.completed = completed

            todoWriteViewModel!!.update(todo)
        } else {
            todoWriteViewModel!!.createTodo(title, description)
        }

        todoWriteViewModel!!.writeTodoOperation.observe(this, Observer { resource ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Looper.getMainLooper().isCurrentThread)
                    throw AssertionError("")
            } else {
                if (Looper.getMainLooper() != Looper.myLooper())
                    throw AssertionError("")
            }

            when {
                resource.isLoading -> displayLoader()
                resource.data != null -> {

                    Toast.makeText(this@TodoCreateEditActivity, "Todo Created!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> handleErrorResponse(resource.fullMessages)
            }
        })
    }
}
