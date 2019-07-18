package com.melardev.android.crud.todos.list


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melardev.android.crud.R
import com.melardev.android.crud.databinding.MainActivityBinding
import com.melardev.android.crud.datasource.common.entities.Todo
import com.melardev.android.crud.factories.AppViewModelFactory
import com.melardev.android.crud.todos.base.BaseActivity
import com.melardev.android.crud.todos.show.TodoDetailsActivity
import com.melardev.android.crud.todos.write.TodoCreateEditActivity
import javax.inject.Inject

class MainActivity : BaseActivity(), TodoListAdapter.TodoRowEventListener {

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory

    private var binding: MainActivityBinding? = null

    private var todoListViewModel: TodoListViewModel? = null

    private var todoListAdapter: TodoListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        todoListAdapter = TodoListAdapter(this)
        binding!!.rvTodos.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        binding!!.rvTodos.adapter = todoListAdapter
    }

    private fun initViewModel() {

        todoListViewModel = ViewModelProviders.of(this, appViewModelFactory)
            .get(TodoListViewModel::class.java)

        // Observe the list, if
        todoListViewModel!!.todoList.observe(this, Observer {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Looper.getMainLooper().isCurrentThread)
                    throw AssertionError("")
            } else {
                if (Looper.getMainLooper() != Looper.myLooper())
                    throw AssertionError("")
            }

            if (it.isLoading) {
                displayLoader()
            } else if (it.data != null && it.data.isNotEmpty()) {
                populateRecyclerView(it.data)
            } else
                handleErrorResponse(it.fullMessages)
        })

        todoListViewModel!!.loadTodos()
    }



    private fun populateRecyclerView(todos: List<Todo>?) {
        hideLoader()
        binding!!.rvTodos.visibility = View.VISIBLE
        todoListAdapter!!.setItems(todos)
    }

    fun createTodo(view: View) {
        val intent = Intent(this, TodoCreateEditActivity::class.java)
        startActivity(intent)
    }

    override fun onClicked(todo: Todo) {
        val intent = Intent(this, TodoDetailsActivity::class.java)
        intent.putExtra("TODO_ID", todo.id)
        startActivity(intent)
    }
}
