package com.example.roomwithaview

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoitems_table")
data class ToDoItem(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name="content") val content:String,
    @ColumnInfo(name="due_date") val dueDate:Long?,
    @ColumnInfo(name="completed") val completed:Int
    )
