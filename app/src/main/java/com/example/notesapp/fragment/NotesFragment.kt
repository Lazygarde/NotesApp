package com.example.notesapp.fragment

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.adapter.NoteRVAdapter
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteViewModel
import com.example.notesapp.databinding.FragmentNotesBinding
import com.example.notesapp.interfaces.NoteClickInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class NotesFragment : Fragment(), NoteClickInterface {
    private lateinit var noteRV: RecyclerView
    private lateinit var binding: FragmentNotesBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var sortIV: ImageView
    private lateinit var noteRVAdapter: NoteRVAdapter
    private lateinit var listNote: LiveData<List<Note>>
    private lateinit var popupMenu: PopupMenu
    private lateinit var addFAB: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).bnTab.visibility = View.VISIBLE
        noteRV = binding.idRVNotes
        sortIV = binding.ivSort
        addFAB = binding.fab
        setUpSearchView()
        setUpAddFAB()
        setUpAdapter()
        setUpSwipe()
        onSortIVClick()
    }

    private fun setUpAdapter() {
        noteRV.layoutManager = LinearLayoutManager(context)
        noteRVAdapter = NoteRVAdapter(this, this)
        noteRV.adapter = noteRVAdapter
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        listNote = viewModel.listNote
        listNote.observe(viewLifecycleOwner) { list ->
            noteRVAdapter.updateList(list)
        }
    }

    private fun setUpAddFAB() {
        addFAB.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContent, AddEditNoteFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchDB(newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchDB(query)
                }
                return true
            }
        })
    }

    private fun searchDB(query: String) {
        val searchQuery = "%$query%"
        val listNote = viewModel.searchByTitle(searchQuery)
        listNote.observe(viewLifecycleOwner) { list ->
            noteRVAdapter.updateList(list)
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
                val note = noteRVAdapter.getNoteAt(viewHolder.layoutPosition)
                note.isDeleted = 1
                viewModel.updateNote(note)
                listNote.observe(viewLifecycleOwner) { list ->
                    noteRVAdapter.updateList(list)
                }
                Snackbar.make(
                    binding.idRVNotes,
                    "${note.noteTitle} has just been deleted!",
                    Snackbar.LENGTH_LONG
                ).setAction("Undo") {
                    note.isDeleted = 0
                    viewModel.updateNote(note)
                    listNote.observe(viewLifecycleOwner) { list ->
                        noteRVAdapter.updateList(list)
                    }
                }.show()
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
                        ContextCompat.getColor(context!!, R.color.ios_red)
                    )
                    .addSwipeRightActionIcon(R.drawable.ic_delete_swipe)
                    .create()
                    .decorate()
                super.onChildDraw(
                    c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive
                )
            }
        }).attachToRecyclerView(binding.idRVNotes)
    }

    override fun onNoteClick(note: Note) {
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
                                list.timeRemaining
                            }
                            noteRVAdapter.updateList(sortedList)
                        }
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.sort_menu)
            popupMenu.show()
        }
    }
}