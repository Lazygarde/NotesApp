package com.example.notesapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    val listNote: LiveData<List<Note>>
    val deletedNote: LiveData<List<Note>>
    private val repository: NoteRepository

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NoteRepository(dao)
        listNote = repository.listNotes
        deletedNote = repository.deletedNotes
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun searchByTitle(title: String): LiveData<List<Note>> = repository.searchByTitle(title)

    fun calendarSearch(mDate: String): LiveData<List<Note>> = repository.calendarSearch(mDate)
}
