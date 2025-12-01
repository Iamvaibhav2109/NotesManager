package com.neosoft.notesmanager.ui.notes.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.neosoft.notesmanager.viewmodel.NotesViewModel

class ComposeNotesFragment : Fragment() {
    private lateinit var viewmodel: NotesViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewmodel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    val notes by viewmodel.notes.collectAsState()
                    ComposeNotesScreen(
                        notes = notes,
                        onAdd = { t, b, tags -> viewmodel.addNote(t, b, tags) },
                        onDelete = { viewmodel.delete(it) })
                }
            }
        }
    }
}
