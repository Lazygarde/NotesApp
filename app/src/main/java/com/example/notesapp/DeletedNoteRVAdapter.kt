package com.example.notesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.DeletedNoteRvItemBinding

class DeletedNoteRVAdapter(
    val context: DeleteFragment,
    val noteClickInterface: NoteClickInterface,
    val noteClickDeleteInterface: NoteClickDeleteInterface,
    val receiveToGarbage: (Note) -> Unit
) : RecyclerView.Adapter<DeletedNoteRVAdapter.ViewHolder>() {

    private val allNotes = ArrayList<Note>()

    inner class ViewHolder(binding: DeletedNoteRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val noteTV: TextView = binding.tdTVNoteTitle
        val timeTV: TextView = binding.tdTVTimeStamp
        val deleteTV: ImageView = binding.idIVDelete
        val restoreTV: ImageView = binding.idIVRestore
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
        holder.timeTV.text = "Last Update: " + allNotes[position].timeStamp
        holder.restoreTV.setOnClickListener {
            allNotes[position].isDeleted = 0
            sendData(allNotes[position])
        }
        holder.deleteTV.setOnClickListener {
            noteClickDeleteInterface.onDeleteIconClick(allNotes[position])
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
    private fun sendData(note:Note){
        receiveToGarbage(note)
    }
}

