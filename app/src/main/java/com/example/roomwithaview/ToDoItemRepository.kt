package com.example.roomwithaview

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ToDoItemRepository (private val toDoItemDao: ToDoItemDao){

    val allToDoItems: Flow<List<ToDoItem>> = toDoItemDao.getToDoItems()

    @Suppress("RedudndantSuspendModifier")
    @WorkerThread
    suspend fun insert(toDoItem: ToDoItem){
        toDoItemDao.insert(toDoItem)
    }

}