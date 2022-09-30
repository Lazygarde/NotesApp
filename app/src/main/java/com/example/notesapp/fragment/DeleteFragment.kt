package com.example.notesapp.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.adapter.DeletedNoteRVAdapter
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteViewModel
import com.example.notesapp.databinding.FragmentDeleteBinding
import com.example.notesapp.inter.NoteClickDeleteInterface
import com.example.notesapp.inter.NoteClickInterface
import com.example.notesapp.inter.UpdateNoteInterface

class DeleteFragment : Fragment(), NoteClickInterface, NoteClickDeleteInterface,
    UpdateNoteInterface {
    private lateinit var noteRV: RecyclerView
    private lateinit var binding: FragmentDeleteBinding
    private lateinit var viewModel: NoteViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteRV = binding.idRVDeletedNotes
        noteRV.layoutManager = LinearLayoutManager(context)

        val deletedNoteRVAdapter = DeletedNoteRVAdapter(this, this, this, this)
        noteRV.adapter = deletedNoteRVAdapter
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        viewModel.deletedNote.observe(viewLifecycleOwner) { list ->
            deletedNoteRVAdapter.updateList(list)
        }
    }

    override fun onNoteClick(note: Note) {
        val view = View.inflate((activity as MainActivity), R.layout.edit_deleted_note_dialog, null)

        val builder = AlertDialog.Builder((activity as MainActivity))
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)

        val cancelBtn = view.findViewById<TextView>(R.id.cancel_btn)
        val recoverBtn = view.findViewById<TextView>(R.id.recover_btn)
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        recoverBtn.setOnClickListener {
            dialog.dismiss()
            note.isDeleted = 0
            viewModel.updateNote(note)
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
    }

    override fun onDeleteIconClick(note: Note) {
        val view = View.inflate((activity as MainActivity), R.layout.deleted_note_dialog, null)
        val builder = AlertDialog.Builder((activity as MainActivity))
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)

        val cancelBT = view.findViewById<TextView>(R.id.cancelBT)
        val deleteBT = view.findViewById<TextView>(R.id.deleteBT)

        cancelBT.setOnClickListener {
            dialog.dismiss()
        }
        deleteBT.setOnClickListener {
            dialog.dismiss()
            viewModel.deleteNote(note)
        }
    }

    override fun onUpdateNote(note: Note) {
        viewModel.updateNote(note)
    }
}
