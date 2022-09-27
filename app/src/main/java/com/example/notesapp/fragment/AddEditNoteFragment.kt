package com.example.notesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
    private lateinit var noteType : String
    private var noteID: Int = 0
    private var noteBackground: Int = 0

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
        }

        checkIV.setOnClickListener {
            (activity as MainActivity).hideKeyBoard()
        }

        doneTV.setOnClickListener {
            backOnClick()
        }

        backIV.setOnClickListener {
            backOnClick()
        }
    }

    private fun backOnClick(){
        val noteTitle = noteTitleEdt.text.toString()
        val noteDescription = noteDescriptionEdt.text.toString()
        val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm", Locale.getDefault())
        val currentDate: String = sdf.format(Date())
        if (noteType == "Edit") {
            if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                val updateNote =
                    Note(noteTitle, noteDescription, currentDate, noteBackground, 0, 0)
                updateNote.id = noteID
                viewModel.updateNote(updateNote)
            }
        } else {
            if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                viewModel.addNote(
                    Note(
                        noteTitle,
                        noteDescription,
                        currentDate,
                        getRanDomColor(),
                        0,
                        0
                    )
                )
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragmentContent, UncheckFragment::class.java, null)
            .addToBackStack(null)
            .commit()
        (activity as MainActivity).bnTab.selectedItemId = R.id.uncheckFragment
        (activity as MainActivity).btAppBar.visibility = View.VISIBLE
        (activity as MainActivity).showFloatingActionButton((activity as MainActivity).addFAB)
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
}