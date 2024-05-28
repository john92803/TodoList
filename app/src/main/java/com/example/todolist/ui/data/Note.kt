package com.example.todolist.ui.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "note")
    val note: String

)
