package com.melardev.android.crud.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.melardev.android.crud.di.ViewModelKey
import com.melardev.android.crud.factories.AppViewModelFactory
import com.melardev.android.crud.todos.list.TodoListViewModel
import com.melardev.android.crud.todos.show.TodoDetailsViewModel
import com.melardev.android.crud.todos.write.TodoWriteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TodoListViewModel::class)
    abstract fun bindTodoListViewModel(todoListViewModel: TodoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodoWriteViewModel::class)
    abstract fun bindTodoWriteViewModel(todoWriteViewModel: TodoWriteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodoDetailsViewModel::class)
    abstract fun bindTodoDetailsViewModel(todoWriteViewModel: TodoDetailsViewModel): ViewModel
}
