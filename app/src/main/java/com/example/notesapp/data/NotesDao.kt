package com.example.notesapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("Select * from notesTable Where isDeleted = 0")
    fun getListNotes(): LiveData<List<Note>>

    @Query("Select * from notesTable Where isDeleted = 1")
    fun getDeletedNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notesTable WHERE noteTitle LIKE :title AND isDeleted = 0")
    fun searchByTitle(title: String): LiveData<List<Note>>

    @Query("SELECT * FROM notesTable WHERE date LIKE :mDate AND isDeleted = 0")
    fun calendarSearch(mDate: String): LiveData<List<Note>>
}
