package com.example.notesapp.inter

import com.example.notesapp.data.Note

interface NoteClickDeleteInterface {
    fun onDeleteIconClick(note: Note)
}