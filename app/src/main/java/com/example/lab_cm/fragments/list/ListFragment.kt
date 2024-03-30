package com.example.lab_cm.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.lab_cm.R
import com.example.lab_cm.data.vm.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class ListFragment : Fragment() {
    private  lateinit var mNoteViewModel: NoteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Recyclerview
        val adapter = ListAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // UserViewModel
        mNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        mNoteViewModel.readAllNotes.observe(viewLifecycleOwner, Observer { note ->
            adapter.setData(note)
        })

        val button = view.findViewById<FloatingActionButton>(R.id.btn_add_new_note_from_list)
        button.setOnClickListener(){
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        val changeLanguageButton = view.findViewById<Button>(R.id.change_language_button)
        changeLanguageButton.setOnClickListener {
            showChangeLanguageDialog()
        }

        return view
    }

    private fun showChangeLanguageDialog() {
        // Dummy languages array
        val languages = arrayOf("English", "Português")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.change_language))
        builder.setSingleChoiceItems(languages, -1) { dialog, which ->
            val locale = when (which) {
                0 -> Locale("en")
                1 -> Locale("pt")
                else -> Locale.getDefault()
            }
            changeLocale(locale)
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun changeLocale(locale: Locale) {
        Locale.setDefault(locale)
        val config = requireContext().resources.configuration
        config.setLocale(locale)
        requireContext().createConfigurationContext(config)
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
        requireActivity().recreate()
    }
}