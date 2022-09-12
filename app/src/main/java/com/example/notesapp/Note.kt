package com.example.notesapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notesTable")
class Note(
    val noteTitle: String,
    val noteDescription: String,
    val timeStamp: String,
    var isDeleted: Int,
    var isDone: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}