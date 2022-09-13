package com.example.notesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.FragmentUncheckBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UncheckFragment : Fragment(), NoteClickInterface, NoteClickDeleteInterface {
    lateinit var noteRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    private lateinit var binding: FragmentUncheckBinding
    lateinit var viewModel: NoteViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUncheckBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteRV = binding.idRVNotes
        noteRV.layoutManager = LinearLayoutManager(context)

        val noteRVAdapter = NoteRVAdapter(this, this, this) {
            receiveToGarbage(it)
        }
        noteRV.adapter = noteRVAdapter
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        viewModel.listNote.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                noteRVAdapter.updateList(it)
            }
        })

    }

    override fun onNoteClick(note: Note) {
        val bundle = Bundle()
        bundle.putString("noteType", "Edit")
        bundle.putString("noteTitle", note.noteTitle)
        bundle.putString("noteDescription", note.noteDescription)
        bundle.putInt("noteID", note.id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragmentContent, AddEditNoteFragment::class.java, bundle)
            .addToBackStack(null)
            .commit()
    }

    override fun onDeleteIconClick(note: Note) {
        viewModel.deleteNote(note)
    }

    private fun receiveToGarbage(note: Note) {
        viewModel.updateNote(note)
    }
}