package com.neosoft.notesmanager.data.localstorage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val body: String,
    val tags: String,
    val createdAt: Long = System.currentTimeMillis()
)