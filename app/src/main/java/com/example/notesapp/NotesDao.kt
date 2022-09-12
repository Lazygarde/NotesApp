package com.example.notesapp

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

    @Query("Select * from notesTable Where isDeleted = 0 and isDone = 0")
    fun getListNotes(): LiveData<List<Note>>

    @Query("Select * from notesTable Where isDeleted = 0 and isDone = 1")
    fun getDoneNotes(): LiveData<List<Note>>

    @Query("Select * from notesTable Where isDeleted = 1")
    fun getDeletedNotes(): LiveData<List<Note>>
}