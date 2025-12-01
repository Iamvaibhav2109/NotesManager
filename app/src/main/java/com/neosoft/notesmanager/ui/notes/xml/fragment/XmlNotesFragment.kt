package com.neosoft.notesmanager.ui.notes.xml.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.neosoft.notesmanager.R
import com.neosoft.notesmanager.databinding.FragmentNotesBinding
import com.neosoft.notesmanager.ui.notes.xml.adapter.NotesAdapter
import com.neosoft.notesmanager.ui.notes.xml.bottomsheet.AddNoteBottomSheet
import com.neosoft.notesmanager.viewmodel.NotesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class XmlNotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewmodel: NotesViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewmodel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)

        setupRecyclerView()
        setupFab()
        setupSearchBar()
        observeNotes()
    }
    private fun setupRecyclerView() {
        adapter = NotesAdapter { note -> viewmodel.delete(note) }
        binding.rvNotes.isNestedScrollingEnabled = false
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotes.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            AddNoteBottomSheet().show(childFragmentManager, "addNote")
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setupSearchBar() {
        binding.searchView.apply {
            queryHint = "Search notes by title, body, or tags..."
            isIconified = false
            clearFocus()
            isFocusable = false
            isFocusableInTouchMode = false
        }
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchView.isFocusable = true
                binding.searchView.isFocusableInTouchMode = true
            }
        }

        val searchText = binding.searchView.findViewById<SearchView.SearchAutoComplete>(
            androidx.appcompat.R.id.search_src_text
        )

        searchText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.search_hint))
        searchText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
        searchText.setPadding(0, 0, 0, 0)
        searchText.textSize = 15f

        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText.orEmpty())
                return true
            }
        })
    }

    private fun observeNotes() {
        lifecycleScope.launch {
            viewmodel.notes.collectLatest { notes ->
                adapter.setData(notes)
                val tags = notes.flatMap { it.tags.split(",") }
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .distinct()

                binding.tagContainer.removeAllViews()

                tags.forEach { tag ->
                    val chip = LayoutInflater.from(requireContext())
                        .inflate(R.layout.tags_chip, binding.tagContainer, false) as TextView

                    chip.text = tag

                    chip.setOnClickListener {
                        binding.searchView.setQuery(tag, false)
                        adapter.filter(tag)
                    }

                    binding.tagContainer.addView(chip)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
