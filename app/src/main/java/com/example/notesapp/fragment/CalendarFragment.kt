package com.example.notesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.adapter.CalendarRVAdapter
import com.example.notesapp.data.NoteViewModel
import com.example.notesapp.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: CalendarRVAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setUpCalendar()
    }

    private fun setUpAdapter() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CalendarRVAdapter(this)
        recyclerView.adapter = adapter
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
    }

    private fun setUpCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            val date = String.format("%02d.%02d.%04d", day, month + 1, year)
            val listNote = viewModel.calendarSearch(date)
            listNote.observe(viewLifecycleOwner) { list ->
                val sortedList = list.sortedBy { allNotes ->
                    allNotes.time
                }
                adapter.updateList(sortedList)
            }
        }
    }
}