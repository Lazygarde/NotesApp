package com.example.notesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.adapter.NoteRVAdapter
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteViewModel
import com.example.notesapp.databinding.FragmentUncheckBinding
import com.example.notesapp.inter.NoteClickInterface
import com.example.notesapp.inter.UpdateNoteInterface

class UncheckFragment : Fragment(), NoteClickInterface, UpdateNoteInterface {
    private lateinit var noteRV: RecyclerView
    private lateinit var binding: FragmentUncheckBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var sortIV: ImageView
    private lateinit var noteRVAdapter: NoteRVAdapter
    private lateinit var listNote: LiveData<List<Note>>
    private lateinit var popupMenu: PopupMenu
    private lateinit var addNoteIV: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUncheckBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addNoteIV = binding.addNoteIV
        noteRV = binding.idRVNotes
        sortIV = binding.ivSort
        noteRV.layoutManager = LinearLayoutManager(context)
        noteRVAdapter = NoteRVAdapter(this, this, this)
        noteRV.adapter = noteRVAdapter
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        listNote = viewModel.listNote
        viewModel.listNote.observe(viewLifecycleOwner) { list ->
            noteRVAdapter.updateList(list)
        }
        onSortIVClick()
    }

    override fun onNoteClick(note: Note) {
        val bundle = Bundle()
        bundle.putString("noteType", "Edit")
        bundle.putString("noteTitle", note.noteTitle)
        bundle.putString("noteDescription", note.noteDescription)
        bundle.putInt("noteID", note.id)
        bundle.putInt("noteBackground", note.backGroundColor)
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragmentContent, AddEditNoteFragment::class.java, bundle)
            .addToBackStack(null)
            .commit()
    }

    private fun onSortIVClick() {
        sortIV.setOnClickListener {
            popupMenu = PopupMenu((activity as MainActivity), sortIV)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.sortById -> {
                        listNote.observe(viewLifecycleOwner) { allNotes ->
                            val sortedList = allNotes.sortedBy { list ->
                                list.id
                            }
                            noteRVAdapter.updateList(sortedList)
                        }
                        true
                    }
                    R.id.sortByTitle -> {
                        listNote.observe(viewLifecycleOwner) { allNotes ->
                            val sortedList = allNotes.sortedBy { list ->
                                list.noteTitle
                            }
                            noteRVAdapter.updateList(sortedList)
                        }
                        true
                    }
                    R.id.sortByDate -> {
                        listNote.observe(viewLifecycleOwner) { allNotes ->
                            val sortedList = allNotes.sortedBy { list ->
                                list.timeStamp
                            }
                            noteRVAdapter.updateList(sortedList)
                        }
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.colormenu)
            popupMenu.show()
        }
    }

    override fun onUpdateNote(note: Note) {
        viewModel.updateNote(note)
    }

}