package com.melardev.android.crud.todos.show

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.melardev.android.crud.R
import com.melardev.android.crud.databinding.TodoDetailsBinding
import com.melardev.android.crud.datasource.common.models.DataSourceOperation
import com.melardev.android.crud.datasource.remote.dtos.responses.SuccessResponse
import com.melardev.android.crud.factories.AppViewModelFactory
import com.melardev.android.crud.todos.base.BaseActivity
import com.melardev.android.crud.todos.write.TodoCreateEditActivity
import javax.inject.Inject

class TodoDetailsActivity : BaseActivity() {

    private var binding: TodoDetailsBinding? = null
    private var todoId: Long = 0
    private var todoDetailsViewModel: TodoDetailsViewModel? = null

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent

        todoId = intent.getLongExtra("TODO_ID", -1)
        initView()
        initViewModel()
    }

    private fun initViewModel() {

        todoDetailsViewModel = ViewModelProviders.of(this, appViewModelFactory)
            .get(TodoDetailsViewModel::class.java)

        // Observe the list, if
        todoDetailsViewModel!!.todo.observe(this, Observer {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Looper.getMainLooper().isCurrentThread)
                    throw AssertionError("")
            } else {
                if (Looper.getMainLooper() != Looper.myLooper())
                    throw AssertionError("")
            }

            when {
                it.isLoading -> {
                    displayLoader()
                }
                it.data != null -> {
                    val todo = it.data
                    binding!!.txtDetailsId.text = todo.id.toString()
                    binding!!.txtDetailsTitle.text = todo.title
                    binding!!.txtDetailsDescription.text = todo.description
                    binding!!.checkboxCompleted.isChecked = todo.completed
                }
                else -> {
                    handleErrorResponse(it.fullMessages)
                }
            }
        })

        todoDetailsViewModel!!.loadTodo(todoId)

    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_details)
    }

    private fun delete() {

        val alertDialog = AlertDialog.Builder(this)
            .setMessage("Are you sure You want to delete this todo?")
            .setPositiveButton(
                "Yes"
            ) { _, _ ->
                todoDetailsViewModel!!.deleteById(todoId)
                todoDetailsViewModel!!.getDeleteOperation().observe(this@TodoDetailsActivity,
                    Observer<DataSourceOperation<SuccessResponse>> { response ->
                        if (response.isSuccess) {
                            Toast.makeText(this@TodoDetailsActivity, "Todo Deleted successfully", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        } else if (response.isLoading) {
                            displayLoader()
                        } else {
                            if (response.fullMessages != null) {
                                Toast.makeText(
                                    this@TodoDetailsActivity,
                                    TextUtils.join(",", response.fullMessages), Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
            .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            .show()
    }

    fun onButtonClicked(view: View) {

        if (binding!!.btnDetailsEditTodo === view) {
            val intent = Intent(this, TodoCreateEditActivity::class.java)
            intent.putExtra("TODO_ID", todoId)
            startActivity(intent)
        } else if (binding!!.btnDetailsDeleteTodo === view) {
            delete()
        } else if (binding!!.btnDetailsGoHome === view) {
            finish()
        }

    }
}

