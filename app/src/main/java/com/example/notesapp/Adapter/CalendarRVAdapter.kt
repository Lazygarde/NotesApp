package com.example.notesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.data.Note
import com.example.notesapp.databinding.CalendarRvItemBinding
import com.example.notesapp.fragment.CalendarFragment

class CalendarRVAdapter(val context: CalendarFragment) :
    RecyclerView.Adapter<CalendarRVAdapter.ViewHolder>() {

    private val allNotes = ArrayList<Note>()

    inner class ViewHolder(binding: CalendarRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val timeReminder = binding.timeReminder
        val title = binding.titleTV
        val noteContainer: CardView = binding.noteContainer
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarRVAdapter.ViewHolder {
        return ViewHolder(
            CalendarRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CalendarRVAdapter.ViewHolder, position: Int) {
        holder.title.text = allNotes[position].noteTitle
        holder.timeReminder.text = allNotes[position].time
        holder.noteContainer.setCardBackgroundColor(holder.itemView.resources.getColor(allNotes[position].backGroundColor))

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