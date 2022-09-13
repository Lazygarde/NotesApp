package com.example.notesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.databinding.FragmentAddEditNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteFragment : Fragment() {
    lateinit var binding: FragmentAddEditNoteBinding
    lateinit var noteTitleEdt: EditText
    lateinit var noteDescriptionEdt: EditText
    lateinit var backIV: ImageView
    lateinit var viewModel: NoteViewModel
    lateinit var checkIV: ImageView
    lateinit var doneTV : TextView
    var noteID = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val noteType = bundle?.getString("noteType")
        if (noteType.equals("Edit")) {
            val noteTitle = bundle?.getString("noteTitle")
            val noteDesc = bundle?.getString("noteDescription")
            noteID = bundle?.getInt("noteID", 0)!!
            //addUpdateBtn.text = "Update Note"
            noteTitleEdt.setText(noteTitle)
            noteDescriptionEdt.setText(noteDesc)
        } else {
            //addUpdateBtn.text = "Save Note"
        }

        checkIV.setOnClickListener {
            (activity as MainActivity).hideKeyBoard()
        }

        doneTV.setOnClickListener{
            (activity as MainActivity).hideKeyBoard()
        }

        backIV.setOnClickListener {
            val noteTitle = noteTitleEdt.text.toString()
            val noteDescription = noteDescriptionEdt.text.toString()
            if (noteType.equals("Edit")) {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.getDefault())
                    val currentDate: String = sdf.format(Date())
                    val updateNote = Note(noteTitle, noteDescription, currentDate, 0, 0)
                    updateNote.id = noteID
                    viewModel.updateNote(updateNote)
                    Toast.makeText(activity, "Note Updated..", Toast.LENGTH_SHORT).show()

                }
            } else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.getDefault())
                    val currentDate: String = sdf.format(Date())
                    viewModel.addNote(Note(noteTitle, noteDescription, currentDate, 0, 0))
                    Toast.makeText(activity, "Note Added..", Toast.LENGTH_SHORT).show()
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragmentContent, UncheckFragment::class.java, null)
                .addToBackStack(null)
                .commit()
            (activity as MainActivity).btAppBar.visibility = View.VISIBLE
            (activity as MainActivity).showFloatingActionButton((activity as MainActivity).addFAB)
        }
    }
}