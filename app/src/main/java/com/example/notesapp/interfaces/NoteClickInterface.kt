package com.example.notesapp.interfaces

import com.example.notesapp.data.Note

interface NoteClickInterface {
    fun onNoteClick(note: Note)
}