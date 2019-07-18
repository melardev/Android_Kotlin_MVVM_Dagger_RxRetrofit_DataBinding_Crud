package com.melardev.android.crud.todos.show

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melardev.android.crud.datasource.common.entities.Todo
import com.melardev.android.crud.datasource.common.models.DataSourceOperation
import com.melardev.android.crud.datasource.common.repositories.TodoRepository
import com.melardev.android.crud.datasource.remote.dtos.responses.SuccessResponse
import javax.inject.Inject

class TodoDetailsViewModel @Inject constructor(var todoRepository: TodoRepository) : ViewModel() {

    private val todoLiveData = MutableLiveData<DataSourceOperation<Todo>>()
    private val deleteLiveData = MutableLiveData<DataSourceOperation<SuccessResponse>>()

    val todo: LiveData<DataSourceOperation<Todo>>
        get() = todoLiveData

    fun loadTodo(todoId: Long) {
        todoRepository.getById(todoId)
            .subscribe { resource -> todoLiveData.postValue(resource) }
    }

    fun getDeleteOperation(): LiveData<DataSourceOperation<SuccessResponse>> {
        return deleteLiveData
    }

    @SuppressLint("CheckResult")
    fun deleteById(todoId: Long) {
        todoRepository.delete(todoId).subscribe { res -> deleteLiveData.postValue(res) }
    }
}
