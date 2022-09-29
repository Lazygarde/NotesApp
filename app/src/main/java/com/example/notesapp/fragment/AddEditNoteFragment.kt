package com.example.notesapp.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
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
    private lateinit var noteTitleEdt: EditText
    private lateinit var noteDescriptionEdt: EditText
    private lateinit var backIV: ImageView
    private lateinit var viewModel: NoteViewModel
    private lateinit var checkIV: ImageView
    private lateinit var doneTV: TextView
    private lateinit var noteType: String
    private var noteID: Int = 0
    private var noteBackground: Int = 0


    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var datePickerDialog: DatePickerDialog
    private var year = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var minute = 0
    private var date = ""
    private var time = ""
    private lateinit var noteTitle: String
    private lateinit var noteDescription: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditNoteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).btAppBar.visibility = View.GONE
        (activity as MainActivity).hideFloatingActionButton((activity as MainActivity).addFAB)
        noteTitleEdt = binding.idEdtNoteTitle
        noteDescriptionEdt = binding.idEdtNoteDescription
        backIV = binding.ivICBack
        checkIV = binding.ivICCheck
        doneTV = binding.tvDone
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        val bundle = this.arguments
        noteType = bundle?.getString("noteType").toString()
        if (noteType == "Edit") {
            noteID = bundle?.getInt("noteID", 0)!!
            noteBackground = bundle.getInt("noteBackground", R.color.colorRandomBG9)
            noteTitleEdt.setText(bundle.getString("noteTitle"))
            noteDescriptionEdt.setText(bundle.getString("noteDescription"))
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
//            val layoutInflater = context?.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val popupView: View = layoutInflater.inflate(R.layout.fill_color_pop_up_menu, null)
//            val popupWindow = PopupWindow(
//                popupView,
//                ActionMenuView.LayoutParams.WRAP_CONTENT,
//                ActionMenuView.LayoutParams.WRAP_CONTENT, true
//            )
//            popupWindow.isOutsideTouchable = true
//            popupWindow.isFocusable = true
//            popupWindow.setBackgroundDrawable(BitmapDrawable())
//            val parent = requireView().rootView
//            popupWindow.showAtLocation(parent, Gravity.getAbsoluteGravity(Gravity.NO_GRAVITY, ), 100, 100)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getTime() {
        binding.timePicker.setOnTouchListener { _: View?, motionEvent: MotionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                val calendar = Calendar.getInstance()
                hour = calendar.get(Calendar.HOUR)
                minute = calendar.get(Calendar.MINUTE)
                timePickerDialog = TimePickerDialog(
                    activity, R.style.TimePickerDialogTheme,
                    { _: TimePicker?, hourOfDay: Int, minute: Int ->
                        time = String.format("%02d:%02d", hourOfDay, minute)
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
                year = calendar.get(Calendar.YEAR)
                month = calendar.get(Calendar.MONTH)
                day = calendar.get(Calendar.DAY_OF_MONTH)
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

    private fun backOnClick() {
        val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.getDefault())
        val currentDate: String = sdf.format(Date())
        noteTitle = noteTitleEdt.text.toString()
        noteDescription = noteDescriptionEdt.text.toString()
        if (checkValid()) {
            if (noteType == "Edit") {
                val updateNote =
                    Note(noteTitle, noteDescription, currentDate, noteBackground, 0, 0, time, date)
                updateNote.id = noteID
                viewModel.updateNote(updateNote)
            } else {
                viewModel.addNote(
                    Note(
                        noteTitle,
                        noteDescription,
                        currentDate,
                        getRanDomColor(),
                        0,
                        0,
                        time, date
                    )
                )
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragmentContent, UncheckFragment::class.java, null)
                .addToBackStack(null)
                .commit()
            (activity as MainActivity).bnTab.selectedItemId = R.id.uncheckFragment
            (activity as MainActivity).btAppBar.visibility = View.VISIBLE
            (activity as MainActivity).showFloatingActionButton((activity as MainActivity).addFAB)
        }
    }

    private fun getRanDomColor(): Int {
        val listColor = listOf(
            R.color.colorRandomBG6,
            R.color.colorRandomBG7,
            R.color.colorRandomBG8,
            R.color.colorRandomBG9
        )
        return listColor.random()
    }

    private fun checkValid(): Boolean {
        if (noteTitle.isEmpty()) {
            Toast.makeText(context, "Please enter a valid title", Toast.LENGTH_SHORT).show()
            return false
        } else if (noteDescription.isEmpty()) {
            Toast.makeText(context, "Please enter a valid description", Toast.LENGTH_SHORT).show()
            return false
        } else if (time.isEmpty()) {
            Toast.makeText(context, "Please pick the time", Toast.LENGTH_SHORT).show()
            return false
        } else if (date.isEmpty()) {
            Toast.makeText(context, "Please pick the date", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}