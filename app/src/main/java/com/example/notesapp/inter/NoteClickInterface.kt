package com.example.notesapp.inter

import com.example.notesapp.data.Note

interface NoteClickInterface {
    fun onNoteClick(note: Note)
}