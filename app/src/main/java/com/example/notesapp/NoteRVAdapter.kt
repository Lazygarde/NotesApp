package com.example.notesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.NoteRvItemBinding

class NoteRVAdapter(
    val context: UncheckFragment,
    val noteClickInterface: NoteClickInterface,
    val noteClickDeleteInterface: NoteClickDeleteInterface,
    val receiveToGarbage: (Note) -> Unit
) : RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {

    private val allNotes = ArrayList<Note>()

    inner class ViewHolder(binding: NoteRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val noteTV: TextView = binding.tdTVNoteTitle
        val timeTV: TextView = binding.tdTVTimeStamp
        val deleteTV: ImageView = binding.idIVDelete
        val checkTV: ImageView = binding.idIVCheck
        val notesTV: TextView = binding.tdTVNotes
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
            sendData(allNotes[position])
        }
        holder.notesTV.text = allNotes[position].noteDescription
        holder.deleteTV.setOnClickListener {
            allNotes[position].isDeleted = 1
            sendData(allNotes[position])
            //noteClickDeleteInterface.onDeleteIconClick(allNotes[position])
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

    private fun sendData(note: Note) {
        receiveToGarbage(note)
    }
}

interface NoteClickDeleteInterface {
    fun onDeleteIconClick(note: Note)
}

interface NoteClickInterface {
    fun onNoteClick(note: Note)
}

