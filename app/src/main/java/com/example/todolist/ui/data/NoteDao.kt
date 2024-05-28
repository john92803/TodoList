package com.example.todolist.ui.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * from item ORDER BY date DESC")
    fun getItems(): Flow<List<Note>>

    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Note>

    @Query("SELECT * from item WHERE date Like :date ORDER BY date DESC")
    fun getNoteByDate(date: String): Flow<List<Note>>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}