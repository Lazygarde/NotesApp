package com.example.notesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.adapter.CalendarRVAdapter
import com.example.notesapp.data.NoteViewModel
import com.example.notesapp.databinding.FragmentCalendarBinding
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import java.text.SimpleDateFormat
import java.util.*

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
        setupEvents()
    }

    private fun setUpAdapter() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CalendarRVAdapter(this)
        recyclerView.adapter = adapter
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
    }

    private fun setUpCalendar() {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val currentDate: String = sdf.format(Date())
        binding.textMonth.text =
            SimpleDateFormat("MMMM - yyyy", Locale.getDefault()).format(Date())
        binding.calendarView.shouldDrawIndicatorsBelowSelectedDays(true)
        showCalendarItemRecyclerView(currentDate)
        binding.calendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(date: Date) {
                val searchQuery = sdf.format(date)
                showCalendarItemRecyclerView(searchQuery)
            }

            override fun onMonthScroll(date: Date) {
                binding.textMonth.text =
                    SimpleDateFormat("MMMM - yyyy", Locale.getDefault()).format(date)
            }
        })
    }

    private fun setupEvents() {
        val listNote = viewModel.listNote
        val set = mutableSetOf<Long>()
        listNote.observe(viewLifecycleOwner) { notes ->
            for (note in notes) {
                val calendar = Calendar.getInstance()
                val date: ArrayList<String> = note.date
                    .split(".") as ArrayList<String>
                val dd: String = date[0]
                val month: String = date[1]
                val year: String = date[2]
                calendar[Calendar.DAY_OF_MONTH] = dd.toInt()
                calendar[Calendar.MONTH] = month.toInt() - 1
                calendar[Calendar.YEAR] = year.toInt()
                val k = calendar.timeInMillis / 86400000
                if (k !in set) {
                    set.add(k)
                    binding.calendarView.addEvent(
                        Event(
                            R.color.black,
                            calendar.timeInMillis
                        )
                    )
                }

            }
        }
    }

    private fun showCalendarItemRecyclerView(date: String) {
        val listNote = viewModel.calendarSearch(date)
        listNote.observe(viewLifecycleOwner) { list ->
            val sortedList = list.sortedBy { allNotes ->
                allNotes.time
            }
            adapter.updateList(sortedList)
        }
    }
}