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
import com.example.notesapp.databinding.FragmentCheckBinding

class CheckFragment : Fragment(), NoteClickInterface, NoteClickDeleteInterface {
    lateinit var noteRV: RecyclerView
    private lateinit var binding: FragmentCheckBinding
    lateinit var viewModel: NoteViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteRV = binding.idRVDoneNotes
        noteRV.layoutManager = LinearLayoutManager(context)

        val deletedNoteRVAdapter = DoneNoteRVAdapter(this, this, this) { receiveToGarbage(it) }
        noteRV.adapter = deletedNoteRVAdapter
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        viewModel.doneNote.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                deletedNoteRVAdapter.updateList(it)
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
