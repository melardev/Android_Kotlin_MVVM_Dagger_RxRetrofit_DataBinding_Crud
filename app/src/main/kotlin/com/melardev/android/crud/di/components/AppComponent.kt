package com.melardev.android.crud.di.components

import android.app.Application
import com.melardev.android.crud.TodoApplication
import com.melardev.android.crud.di.modules.ActivityBuildersModule
import com.melardev.android.crud.di.modules.DataModule
import com.melardev.android.crud.di.modules.NetworkModule
import com.melardev.android.crud.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

import javax.inject.Singleton


@Component(
    modules = [
        DataModule::class,
        NetworkModule::class,
        ViewModelModule::class,
        ActivityBuildersModule::class,
        AndroidSupportInjectionModule::class]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: TodoApplication)
}
