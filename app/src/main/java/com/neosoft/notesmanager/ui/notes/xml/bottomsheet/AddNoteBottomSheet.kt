package com.neosoft.notesmanager.ui.notes.xml.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.lifecycle.ViewModelProvider
import com.neosoft.notesmanager.databinding.BottomSheetAddBinding
import com.neosoft.notesmanager.viewmodel.NotesViewModel

class AddNoteBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewmodel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        viewmodel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)

        binding.btnSave.setOnClickListener {
            val t = binding.etTitle.text.toString().trim()
            val b = binding.etBody.text.toString().trim()
            val tags = binding.etTags.text.toString().trim()

            if (t.isNotEmpty()) {
                viewmodel.addNote(t, b, tags)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
