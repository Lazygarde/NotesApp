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
import com.example.notesapp.adapter.DoneNoteRVAdapter
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteViewModel
import com.example.notesapp.databinding.FragmentCheckBinding
import com.example.notesapp.inter.NoteClickInterface
import com.example.notesapp.inter.UpdateNoteInterface

class CheckFragment : Fragment(), NoteClickInterface, UpdateNoteInterface {
    private lateinit var noteRV: RecyclerView
    private lateinit var binding: FragmentCheckBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var doneNoteRVAdapter: DoneNoteRVAdapter
    private lateinit var sortIV: ImageView
    private lateinit var popupMenu: PopupMenu
    private lateinit var doneNote: LiveData<List<Note>>
    private lateinit var addNoteIV : ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteRV = binding.idRVDoneNotes
        sortIV = binding.ivSort
        addNoteIV = binding.addNoteIV
        noteRV.layoutManager = LinearLayoutManager(context)

        doneNoteRVAdapter = DoneNoteRVAdapter(this, this, this)
        noteRV.adapter = doneNoteRVAdapter
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        doneNote = viewModel.doneNote
        doneNote.observe(viewLifecycleOwner) { list ->
            doneNoteRVAdapter.updateList(list)

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
        bundle.putString("noteTime",note.time)
        bundle.putString("noteDate", note.date)
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
                        doneNote.observe(viewLifecycleOwner) { allNotes ->
                            val sortedList = allNotes.sortedBy { list ->
                                list.id
                            }
                            doneNoteRVAdapter.updateList(sortedList)
                        }
                        true
                    }
                    R.id.sortByTitle -> {
                        doneNote.observe(viewLifecycleOwner) { allNotes ->
                            val sortedList = allNotes.sortedBy { list ->
                                list.noteTitle
                            }
                            doneNoteRVAdapter.updateList(sortedList)
                        }
                        true
                    }
                    R.id.sortByDate -> {
                        doneNote.observe(viewLifecycleOwner) { allNotes ->
                            val sortedList = allNotes.sortedBy { list ->
                                list.timeStamp
                            }
                            doneNoteRVAdapter.updateList(sortedList)
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
