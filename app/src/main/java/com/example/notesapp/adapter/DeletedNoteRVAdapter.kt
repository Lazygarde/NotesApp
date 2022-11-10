package com.example.notesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.data.Note
import com.example.notesapp.databinding.DeletedNoteRvItemBinding
import com.example.notesapp.fragment.DeleteFragment
import com.example.notesapp.interfaces.NoteClickInterface

class DeletedNoteRVAdapter(
    val context: DeleteFragment,
    private val noteClickInterface: NoteClickInterface,
) : RecyclerView.Adapter<DeletedNoteRVAdapter.ViewHolder>() {

    private val allNotes = ArrayList<Note>()

    inner class ViewHolder(binding: DeletedNoteRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val noteTV: TextView = binding.tdTVNoteTitle
        val notesTV: TextView = binding.tdTVNotes
        val noteContainer: CardView = binding.noteContainer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DeletedNoteRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTV.text = allNotes[position].noteTitle
        holder.noteContainer.setCardBackgroundColor(holder.itemView.resources.getColor(allNotes[position].backGroundColor))
        holder.notesTV.text = allNotes[position].noteDescription
        if (holder.notesTV.text == "") {
            holder.notesTV.text = "No additional text"
        }
        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(allNotes[position])
        }
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    fun getNoteAt(position: Int): Note {
        return allNotes[position]
    }

    fun updateList(newList: List<Note>) {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }

}

