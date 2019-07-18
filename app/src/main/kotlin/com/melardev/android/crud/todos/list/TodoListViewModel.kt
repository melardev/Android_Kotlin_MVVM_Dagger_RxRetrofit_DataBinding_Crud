package com.melardev.android.crud.todos.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melardev.android.crud.datasource.common.entities.Todo
import com.melardev.android.crud.datasource.common.models.DataSourceOperation
import com.melardev.android.crud.datasource.common.repositories.TodoRepository
import javax.inject.Inject


class TodoListViewModel @Inject constructor(private var todoRepository: TodoRepository) : ViewModel() {

    private val todoListLiveData = MutableLiveData<DataSourceOperation<List<Todo>>>()

    val todoList: LiveData<DataSourceOperation<List<Todo>>>
        get() = todoListLiveData

    fun loadTodos() {
        todoRepository.getAll()
            .subscribe { resource -> todoListLiveData.postValue(resource) }
    }
}
