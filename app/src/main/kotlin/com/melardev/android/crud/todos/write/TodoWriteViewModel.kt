package com.melardev.android.crud.todos.write

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melardev.android.crud.datasource.common.entities.Todo
import com.melardev.android.crud.datasource.common.models.DataSourceOperation
import com.melardev.android.crud.datasource.common.repositories.TodoRepository
import javax.inject.Inject

class TodoWriteViewModel @Inject
constructor(private val todoRepository: TodoRepository) : ViewModel() {
    private val todoLiveData = MutableLiveData<DataSourceOperation<Todo>>()
    private val writeResponseLiveData = MutableLiveData<DataSourceOperation<Todo>>()

    val todo: LiveData<DataSourceOperation<Todo>>
        get() = todoLiveData

    val writeTodoOperation: LiveData<DataSourceOperation<Todo>>
        get() = writeResponseLiveData

    fun loadTodo(todoId: Long) {
        todoRepository.getById(todoId)
            .subscribe { resource -> todoLiveData.postValue(resource) }
    }

    fun createTodo(title: String, description: String) {
        val todo = Todo()
        todo.title = title
        todo.description = description

        todoRepository.create(todo)
            .subscribe { writeResponse -> writeResponseLiveData.postValue(writeResponse) }
    }

    fun update(todo: Todo) {
        todoRepository.update(todo).subscribe { response -> writeResponseLiveData.postValue(response) }
    }
}
