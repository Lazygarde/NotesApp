package com.example.notesapp.inter

import com.example.notesapp.data.Note

interface UpdateNoteInterface{
    fun onUpdateNote(note: Note)
}