package com.example.notesapp.data

import androidx.lifecycle.LiveData

class NoteRepository(private val notesDao: NotesDao) {
    val listNotes: LiveData<List<Note>> = notesDao.getListNotes()
    val deletedNotes: LiveData<List<Note>> = notesDao.getDeletedNotes()
    val doneNotes : LiveData<List<Note>> = notesDao.getDoneNotes()

    suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    suspend fun delete(note: Note) {
        notesDao.delete(note)
    }

    suspend fun update(note: Note) {
        notesDao.update(note)
    }

    fun searchByTitle(title: String) : LiveData<List<Note>> = notesDao.searchByTitle(title)

    fun searchIsDoneByTitle(title: String) : LiveData<List<Note>> = notesDao.searchIsDoneByTitle(title)

    fun calendarSearch(mDate: String) : LiveData<List<Note>> = notesDao.calendarSearch(mDate)
}