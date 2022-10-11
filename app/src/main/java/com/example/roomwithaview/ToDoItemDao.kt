package com.example.roomwithaview

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoItemDao {

    @Query("SELECT * FROM todoitems_table order by id ASC")
    fun getToDoItems(): Flow<List<ToDoItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(toDoItem: ToDoItem)

    @Query("DELETE FROM todoitems_table")
    suspend fun deleteAll()

}