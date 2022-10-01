package com.example.notesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.data.Note
import com.example.notesapp.databinding.DeletedNoteRvItemBinding
import com.example.notesapp.fragment.DeleteFragment
import com.example.notesapp.inter.NoteClickDeleteInterface
import com.example.notesapp.inter.NoteClickInterface
import com.example.notesapp.inter.UpdateNoteInterface

class DeletedNoteRVAdapter(
    val context: DeleteFragment,
    private val noteClickInterface: NoteClickInterface,
    private val noteClickDeleteInterface: NoteClickDeleteInterface,
    private val updateNoteInterface: UpdateNoteInterface
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
//    private fun getTimeRemain(date: String, time: String): String {
//        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
//        val deadline = sdf.parse("$date $time")
//        val currentTime = sdf.parse(sdf.format(Date()))
//        val remaining = (deadline!!.time - currentTime!!.time) / 60000
//        return if (remaining <= 0) "Time out!"
//        else if (remaining < 60) "$remaining minutes."
//        else {
//            val remain = remaining / 60
//            if (remain < 24) "$remain hours."
//            else "${remain / 24} days."
//        }
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTV.text = allNotes[position].noteTitle
//        holder.timeTV.text = getTimeRemain(allNotes[position].date, allNotes[position].time)
//        holder.restoreTV.setOnClickListener {
//            allNotes[position].isDeleted = 0
//            updateNoteInterface.onUpdateNote(allNotes[position])
//        }
        holder.noteContainer.setCardBackgroundColor(holder.itemView.resources.getColor(allNotes[position].backGroundColor))
        holder.notesTV.text = allNotes[position].noteDescription
//        holder.deleteTV.setOnClickListener {
//            noteClickDeleteInterface.onDeleteIconClick(allNotes[position])
//        }
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

