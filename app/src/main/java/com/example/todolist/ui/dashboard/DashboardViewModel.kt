package com.example.todolist.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.ui.data.Note
import com.example.todolist.ui.data.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import java.time.LocalDate

class DashboardViewModel (private val noteDao: NoteDao): ViewModel() {

    // Cache all items form the database using LiveData.
    val allItems: LiveData<List<Note>> = noteDao.getItems().asLiveData()

    fun findNote(date: String): LiveData<List<Note>> {
        return noteDao.getNoteByDate(date).asLiveData()
    }

    fun findNoteByCount(date: String): Flow<List<Note>> {
        return noteDao.getNoteByToCount(date)
    }

    /**
     * Updates an existing Item in the database.
     */
    fun updateItem(
        noteId: Int,
        date: String,
        note: String
    ) {
        val updatedItem = getUpdatedItemEntry(noteId, date, note)
        updateItem(updatedItem)
    }


    /**
     * Launching a new coroutine to update an item in a non-blocking way
     */
    private fun updateItem(note: Note) {
        viewModelScope.launch {
            noteDao.update(note)
        }
    }

    fun isEntryValid(note:String, date: String): Boolean {
        if (note.isBlank() || date.isBlank() ) {
            return false
        }
        return true
    }


    /**
     * Inserts the new Item into database.
     */
    fun addNewItem(note:String, date: String) {
        val newItem = getNewItemEntry(date, note)
        insertItem(newItem)
    }

    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    private fun insertItem(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    /**
     * Launching a new coroutine to delete an item in a non-blocking way
     */
    fun deleteItem(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }

    /**
     * Retrieve an item from the repository.
     */
    fun retrieveItem(id: Int): LiveData<Note> {
        return noteDao.getItem(id).asLiveData()
    }


    /**
     * Returns an instance of the [Item] entity class with the item info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewItemEntry(note:String, date: String): Note {
        return Note(
            date = date,
            note = note
        )
    }

    /**
     * Called to update an existing entry in the Inventory database.
     * Returns an instance of the [Item] entity class with the item info updated by the user.
     */
    private fun getUpdatedItemEntry(
        noteId: Int,
        date: String,
        note:String
    ): Note {
        return Note(
            id = noteId,
            note = note,
            date = date
        )
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class InventoryViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}