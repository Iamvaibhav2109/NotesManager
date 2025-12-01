package com.neosoft.notesmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.neosoft.notesmanager.data.localstorage.AppDatabase
import com.neosoft.notesmanager.data.localstorage.Notes
import com.neosoft.notesmanager.data.repository.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = NotesRepository(AppDatabase.getInstance(app).notesDao())
    val notes: StateFlow<List<Notes>> = repo.allNotes().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addNote(title: String, body: String, tags: String) {
        viewModelScope.launch {
            repo.add(Notes(title = title, body = body, tags = tags))
        }
    }
    fun delete(note: Notes) {
        viewModelScope.launch { repo.delete(note) }
    }
}
