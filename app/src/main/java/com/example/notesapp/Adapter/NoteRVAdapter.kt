package com.example.notesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.data.Note
import com.example.notesapp.databinding.NoteRvItemBinding
import com.example.notesapp.fragment.UncheckFragment
import com.example.notesapp.inter.NoteClickInterface
import com.example.notesapp.inter.UpdateNoteInterface

class NoteRVAdapter(
    val context: UncheckFragment,
    private val noteClickInterface: NoteClickInterface,
    private val updateNoteInterface: UpdateNoteInterface
) : RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {

    private val allNotes = ArrayList<Note>()

    inner class ViewHolder(binding: NoteRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val noteTV: TextView = binding.tdTVNoteTitle
        val timeTV: TextView = binding.tdTVTimeStamp
        val deleteTV: ImageView = binding.idIVDelete
        val checkTV: ImageView = binding.idIVCheck
        val notesTV: TextView = binding.tdTVNotes
        val noteContainer : CardView = binding.noteContainer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NoteRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTV.text = allNotes[position].noteTitle
        holder.timeTV.text = allNotes[position].timeStamp
        holder.checkTV.setOnClickListener {
            allNotes[position].isDone = 1
            updateNoteInterface.onUpdateNote(allNotes[position])
        }

        holder.noteContainer.setCardBackgroundColor(holder.itemView.resources.getColor(allNotes[position].backGroundColor))
        holder.notesTV.text = allNotes[position].noteDescription
        holder.deleteTV.setOnClickListener {
            allNotes[position].isDeleted = 1
            updateNoteInterface.onUpdateNote(allNotes[position])
        }
        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(allNotes[position])
        }
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    fun updateList(newList: List<Note>) {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }
}