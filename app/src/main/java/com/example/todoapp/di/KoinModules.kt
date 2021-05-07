package com.example.todoapp.di

import android.app.Application
import androidx.room.Room
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.database.dao.TodoDao
import com.example.todoapp.model.TodoViewModel
import com.example.todoapp.model.TodoViewModelFactory
import com.example.todoapp.repository.TodoRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module{
    viewModel{
        TodoViewModel(get())
    }
}

val factoryModule = module{
    factory {
        TodoViewModelFactory(get())
    }
}


val databaseModule = module{
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "eds.database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    fun provideTodoDao(database: AppDatabase): TodoDao {
        return database.todoDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideTodoDao(get()) }
}

val repositoryModule = module {
    fun provideTodoRepository(todoDao: TodoDao): TodoRepository {
        return TodoRepository(todoDao)
    }

    single { provideTodoRepository(get()) }
}

