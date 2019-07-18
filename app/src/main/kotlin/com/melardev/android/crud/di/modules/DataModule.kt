package com.melardev.android.crud.di.modules

import com.google.gson.Gson
import com.melardev.android.crud.datasource.common.repositories.TodoRepository
import com.melardev.android.crud.datasource.remote.api.RxTodoApi
import com.melardev.android.crud.datasource.remote.repositories.RetrofitTodoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    internal fun todoRepository(todoApi: RxTodoApi, gson: Gson): TodoRepository {
        return RetrofitTodoRepository(todoApi, gson)
    }

}
