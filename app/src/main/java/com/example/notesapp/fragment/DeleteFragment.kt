package com.example.notesapp.fragment

import android.app.AlertDialog
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.adapter.DeletedNoteRVAdapter
import com.example.notesapp.adapter.NoteClickInterface
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteViewModel
import com.example.notesapp.databinding.FragmentDeleteBinding
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class DeleteFragment : Fragment(), NoteClickInterface {
    private lateinit var noteRV: RecyclerView
    private lateinit var binding: FragmentDeleteBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var deletedNoteRVAdapter: DeletedNoteRVAdapter
    private lateinit var listNote: LiveData<List<Note>>

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
        setUpAdapter()
        setUpSwipe()
    }

    private fun setUpAdapter() {
        noteRV.layoutManager = LinearLayoutManager(context)
        deletedNoteRVAdapter = DeletedNoteRVAdapter(this, this)
        noteRV.adapter = deletedNoteRVAdapter
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        listNote = viewModel.deletedNote
        listNote.observe(viewLifecycleOwner) { list ->
            deletedNoteRVAdapter.updateList(list)
        }
    }

    private fun setUpSwipe() {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        val note = deletedNoteRVAdapter.getNoteAt(viewHolder.layoutPosition)
                        note.isDeleted = 0
                        viewModel.updateNote(note)
                        listNote.observe(viewLifecycleOwner) { list ->
                            deletedNoteRVAdapter.updateList(list)
                        }
                        Snackbar.make(
                            binding.idRVDeletedNotes,
                            "${note.noteTitle} has just been deleted!",
                            Snackbar.LENGTH_LONG
                        ).setAction("Undo") {
                            note.isDeleted = 1
                            viewModel.updateNote(note)
                            listNote.observe(viewLifecycleOwner) { list ->
                                deletedNoteRVAdapter.updateList(list)
                            }
                        }.show()
                    }
                    ItemTouchHelper.LEFT -> {
                        val note = deletedNoteRVAdapter.getNoteAt(viewHolder.layoutPosition)
                        viewModel.deleteNote(note)
                        val view = View.inflate(
                            (activity as MainActivity),
                            R.layout.deleted_note_dialog,
                            null
                        )
                        val builder = AlertDialog.Builder((activity as MainActivity))
                        builder.setView(view)

                        val dialog = builder.create()
                        dialog.show()
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog.setCancelable(false)

                        val cancelBT = view.findViewById<TextView>(R.id.cancelBT)
                        val deleteBT = view.findViewById<TextView>(R.id.deleteBT)

                        cancelBT.setOnClickListener {
                            viewModel.addNote(note)
                            dialog.dismiss()
                        }
                        deleteBT.setOnClickListener {
                            dialog.dismiss()
                            viewModel.deleteNote(note)
                            Snackbar.make(
                                binding.idRVDeletedNotes,
                                "${note.noteTitle} has just been deleted!",
                                Snackbar.LENGTH_LONG
                            ).setAction("Undo") {
                                viewModel.addNote(note)
                            }.show()
                        }
                    }
                }

            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive
                ).addCornerRadius(1, 10)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(context!!, R.color.ios_red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_swipe)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            context!!, R.color.green
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.ic_recover)
                    .create()
                    .decorate()
                super.onChildDraw(
                    c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive
                )
            }
        }).attachToRecyclerView(binding.idRVDeletedNotes)
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
            bundle.putString("noteTime", note.time)
            bundle.putString("noteDate", note.date)
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragmentContent, AddEditNoteFragment::class.java, bundle)
                .addToBackStack(null)
                .commit()
        }
    }

}
