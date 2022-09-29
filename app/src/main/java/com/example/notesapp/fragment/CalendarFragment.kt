package com.example.notesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.adapter.CalendarRVAdapter
import com.example.notesapp.data.NoteViewModel
import com.example.notesapp.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private lateinit var calendarView: CalendarView
    private lateinit var viewModel: NoteViewModel


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

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CalendarRVAdapter(this)
        recyclerView.adapter = adapter
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]


        calendarView = binding.calendarView
        calendarView.setOnDateChangeListener { _, year, month, day ->
            val date = String.format("%02d.%02d.%04d", day, month + 1, year)
            val listNote = viewModel.calendarSearch(date)
            listNote.observe(viewLifecycleOwner) { list ->
                adapter.updateList(list)
            }
        }
    }

}