package com.melardev.android.crud.di.modules


import com.melardev.android.crud.todos.list.MainActivity
import com.melardev.android.crud.todos.show.TodoDetailsActivity
import com.melardev.android.crud.todos.write.TodoCreateEditActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector()
    abstract fun contributeWriteTodoActivity(): TodoCreateEditActivity

    @ContributesAndroidInjector()
    abstract fun contributeTodoDetailsActivity(): TodoDetailsActivity
}
