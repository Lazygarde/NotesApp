package com.example.notesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.data.Note
import com.example.notesapp.databinding.NoteRvItemBinding
import com.example.notesapp.fragment.NotesFragment
import java.text.SimpleDateFormat
import java.util.*

class NoteRVAdapter(
    val context: NotesFragment,
    private val noteClickInterface: NoteClickInterface
) : RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {

    private val allNotes = ArrayList<Note>()

    inner class ViewHolder(binding: NoteRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val noteTV: TextView = binding.tdTVNoteTitle
        val timeTV: TextView = binding.tdTVTimeStamp
        val notesTV: TextView = binding.tdTVNotes
        val noteContainer: CardView = binding.noteContainer
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
        holder.timeTV.text = getTimeRemain(allNotes[position].date, allNotes[position].time)
        holder.noteContainer.setCardBackgroundColor(holder.itemView.resources.getColor(allNotes[position].backGroundColor))
        holder.notesTV.text = allNotes[position].noteDescription
        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(allNotes[position])
        }
    }

    private fun getTimeRemain(date: String, time: String): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val deadline = sdf.parse("$date $time")
        val currentTime = sdf.parse(sdf.format(Date()))
        var remaining = (deadline!!.time - currentTime!!.time) / 60000
        return if (remaining <= 0) "Time out!"
        else if (remaining < 60) "$remaining minutes."
        else {
            remaining /= 60
            if (remaining < 24) "$remaining hours."
            else {
                remaining /= 24
                if (remaining < 30) "$remaining days."
                else {
                    remaining /= 30
                    if (remaining < 12) "$remaining months."
                    else "${remaining / 12} years."
                }
            }
        }
    }

    fun getNoteAt(position: Int): Note {
        return allNotes[position]
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

interface NoteClickInterface {
    fun onNoteClick(note: Note)
}