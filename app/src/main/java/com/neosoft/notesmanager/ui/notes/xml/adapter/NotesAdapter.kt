package com.neosoft.notesmanager.ui.notes.xml.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.neosoft.notesmanager.R
import com.neosoft.notesmanager.data.localstorage.Notes
import com.neosoft.notesmanager.databinding.ItemNoteBinding

class NotesAdapter(
    private val onDelete: (Notes) -> Unit
) : ListAdapter<Notes, NotesAdapter.VH>(DIFF) {

    private val originalList = mutableListOf<Notes>()

    fun setData(newList: List<Notes>) {
        originalList.clear()
        originalList.addAll(newList)
        submitList(newList.toMutableList())
    }

    fun filter(query: String) {
        val trimmed = query.trim().lowercase()

        if (trimmed.isEmpty()) {
            submitList(originalList.toMutableList())
            return
        }

        val filtered = originalList.filter { note ->
            note.title.lowercase().contains(trimmed) ||
                    note.body.lowercase().contains(trimmed) ||
                    note.tags.lowercase().contains(trimmed)
        }

        submitList(filtered.toMutableList())
    }

    inner class VH(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Notes) {

            binding.txtTitle.text = note.title
            binding.txtBody.text = note.body

            // Build tags dynamically
            binding.tagRow.removeAllViews()
            val tags = note.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }

            tags.forEach { tag ->
                val chip = LayoutInflater.from(binding.root.context)
                    .inflate(R.layout.tags_chip, binding.tagRow, false) as TextView

                chip.text = tag
                binding.tagRow.addView(chip)
            }

            binding.btnDelete.setOnClickListener {
                onDelete(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Notes>() {
            override fun areItemsTheSame(a: Notes, b: Notes) = a.id == b.id
            override fun areContentsTheSame(a: Notes, b: Notes) = a == b
        }
    }
}
