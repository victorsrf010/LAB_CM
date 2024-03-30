package com.example.lab_cm.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lab_cm.R
import com.example.lab_cm.data.entities.Note
import com.example.lab_cm.data.vm.NoteViewModel

class UpdateFragment : Fragment() {
    private  val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mNoteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mNoteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        view.findViewById<TextView>(R.id.updateNote).text = args.currentNote.note

        val updateButton = view.findViewById<Button>(R.id.update)
        updateButton.setOnClickListener {
            updateNote()
        }

        val deleteButton = view.findViewById<Button>(R.id.delete)
        deleteButton.setOnClickListener {
            deleteNote()
        }

        val backButton = view.findViewById<Button>(R.id.backToListFromUpdate)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        return  view
    }

    private  fun updateNote(){
        val noteText = view?.findViewById<EditText>(R.id.updateNote)?.text.toString()

        if(noteText.isEmpty()) {
            makeText(context , getString(R.string.no_empty_note), Toast.LENGTH_LONG).show()
        }
        else {
            val note = Note(args.currentNote.id, noteText)

            mNoteViewModel.updateNote(note)

            makeText(requireContext(), getString(R.string.update_success), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.positive)) { _, _ ->
            mNoteViewModel.deleteNote(args.currentNote)
            makeText(
                requireContext(),
                getString(R.string.delete_success),
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton(getString(R.string.negative)) { _, _ -> }
        builder.setTitle(getString(R.string.delete_title))
        builder.setMessage(getString(R.string.delete_confirm))
        builder.create().show()
    }
}