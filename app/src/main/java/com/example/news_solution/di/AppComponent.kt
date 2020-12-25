package com.example.news_solution.di

import android.app.Application
import com.example.news_solution.BaseApplication
import com.example.news_solution.di.viewmodels.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule


@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuilderModule::class,
        ViewModelModule::class,
        AppModule::class
    ]
)
@AppScope
public interface AppComponent :  AndroidInjector<BaseApplication>{

    @Component.Builder
   public interface Builder {
        @BindsInstance
        fun application(application : Application) : Builder

        fun build() : AppComponent
    }
}