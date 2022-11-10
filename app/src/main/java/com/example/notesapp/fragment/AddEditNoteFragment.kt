package com.example.notesapp.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteViewModel
import com.example.notesapp.databinding.FragmentAddEditNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddEditNoteBinding
    private lateinit var noteDescriptionEdt: EditText
    private lateinit var backIV: ImageView
    private lateinit var viewModel: NoteViewModel
    private lateinit var checkIV: ImageView
    private lateinit var doneTV: TextView
    private lateinit var noteType: String
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var datePickerDialog: DatePickerDialog
    private var date = ""
    private var time = ""
    private var noteID: Int = 0
    private var noteBackground: Int = 0
    private var noteTitle: String = ""
    private var noteDescription: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditNoteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).bnTab.visibility = View.GONE
        noteDescriptionEdt = binding.edtNoteDescription
        backIV = binding.ivICBack
        checkIV = binding.ivICCheck
        doneTV = binding.tvDone
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        val bundle = this.arguments
        noteType = bundle?.getString("noteType").toString()
        if (noteType == "Edit") {
            val s = bundle?.getString("noteTitle") + "\n" + bundle?.getString("noteDescription")
            noteID = bundle?.getInt("noteID", 0)!!
            noteBackground = bundle.getInt("noteBackground", R.color.colorRandomBG9)
            noteDescriptionEdt.setText(s)
            time = bundle.getString("noteTime").toString()
            date = bundle.getString("noteDate").toString()
        }

        checkIV.setOnClickListener {
            (activity as MainActivity).hideKeyBoard()
        }

        doneTV.setOnClickListener {
            backOnClick()
        }

        getDate()
        getTime()
        palettePicker()
        backIV.setOnClickListener {
            backOnClick()
        }
    }

    private fun palettePicker() {
        binding.palettePicker.setOnClickListener {
            val myDialog = Dialog((activity as MainActivity))
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            myDialog.setContentView(R.layout.fill_color_pop_up_menu)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val item1 = myDialog.findViewById(R.id.item1) as ImageView
            val item2 = myDialog.findViewById(R.id.item2) as ImageView
            val item3 = myDialog.findViewById(R.id.item3) as ImageView
            val item4 = myDialog.findViewById(R.id.item4) as ImageView
            val item5 = myDialog.findViewById(R.id.item5) as ImageView
            val item6 = myDialog.findViewById(R.id.item6) as ImageView
            item1.setOnClickListener {
                noteBackground = R.color.item1
                myDialog.dismiss()
            }
            item2.setOnClickListener {
                noteBackground = R.color.item2
                myDialog.dismiss()
            }
            item3.setOnClickListener {
                noteBackground = R.color.item3
                myDialog.dismiss()
            }
            item4.setOnClickListener {
                noteBackground = R.color.item4
                myDialog.dismiss()
            }
            item5.setOnClickListener {
                noteBackground = R.color.item5
                myDialog.dismiss()
            }
            item6.setOnClickListener {
                noteBackground = R.color.item6
                myDialog.dismiss()
            }

            myDialog.window?.attributes?.gravity = Gravity.TOP
            myDialog.show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getTime() {
        binding.timePicker.setOnTouchListener { _: View?, motionEvent: MotionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR)
                val minute = calendar.get(Calendar.MINUTE)
                timePickerDialog = TimePickerDialog(
                    activity, R.style.TimePickerDialogTheme,
                    { _: TimePicker?, hourOfDay: Int, minutes: Int ->
                        time = String.format("%02d:%02d", hourOfDay, minutes)
                        timePickerDialog.dismiss()
                    }, hour, minute, false
                )
                timePickerDialog.show()
            }
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getDate() {
        binding.calendarPicker.setOnTouchListener { _: View?, motionEvent: MotionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                datePickerDialog = DatePickerDialog(
                    requireActivity(), R.style.TimePickerDialogTheme,
                    { _: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int ->
                        date = String.format("%02d.%02d.%04d", dayOfMonth, monthOfYear + 1, year1)
                        datePickerDialog.dismiss()
                    }, year, month, day
                )
                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
            }
            true
        }
    }

    private fun countTimeRemaining(date: String, time: String): Long {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val deadline = sdf.parse("$date $time")
        val currentTime = sdf.parse(sdf.format(Date()))
        return (deadline!!.time - currentTime!!.time) / 60000
    }

    private fun backOnClick() {
        analystNoteDescription()
        if (noteBackground == 0) noteBackground = R.color.item1
        if (time == "") time = "00:00"
        if (date == "") date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        if (noteTitle != "") {
            if (noteType == "Edit") {
                val updateNote =
                    Note(
                        noteTitle, noteDescription, noteBackground, 0,
                        countTimeRemaining(date, time), time, date
                    )
                updateNote.id = noteID
                viewModel.updateNote(updateNote)
            } else {
                viewModel.addNote(
                    Note(
                        noteTitle, noteDescription, noteBackground, 0,
                        countTimeRemaining(date, time), time, date
                    )
                )
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragmentContent, NotesFragment::class.java, null)
            .addToBackStack(null)
            .commit()
        (activity as MainActivity).bnTab.selectedItemId = R.id.uncheckFragment
        (activity as MainActivity).bnTab.visibility = View.VISIBLE
    }

    private fun analystNoteDescription() {
        var s = ""
        var ok = 0
        noteDescription = ""
        val noteDescriptionData = noteDescriptionEdt.text.toString()
        for (i in noteDescriptionData) {
            if (ok == 1) {
                noteDescription += i
            } else {
                if (i == '\n') {
                    noteTitle = s
                    ok = 1
                } else {
                    s += i
                }
            }
        }
        if (ok == 0) {
            noteTitle = s
        }
        noteTitle = noteTitle.trim()
        noteDescription = noteDescription.trim()
    }

}