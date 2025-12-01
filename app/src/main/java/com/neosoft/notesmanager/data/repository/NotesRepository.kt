package com.neosoft.notesmanager.data.repository

import com.neosoft.notesmanager.data.localstorage.Notes
import com.neosoft.notesmanager.data.localstorage.NotesDao
import kotlinx.coroutines.flow.Flow


class NotesRepository(private val dao: NotesDao) {

    fun allNotes(): Flow<List<Notes>> = dao.getAllNotes()

    suspend fun add(note: Notes) = dao.insert(note)
    suspend fun delete(note: Notes) = dao.delete(note)
}