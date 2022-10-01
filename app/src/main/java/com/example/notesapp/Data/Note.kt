package com.example.notesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notesTable")
class Note(
    val noteTitle: String,
    val noteDescription: String,
    val backGroundColor : Int,
    var isDeleted: Int,
    val time: String,
    val date: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}