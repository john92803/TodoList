package com.example.todolist.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.R
import com.example.todolist.databinding.AddNoteBinding
import com.example.todolist.ui.data.Note
import com.example.todolist.ui.data.NoteDao
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale


class AddNote : Fragment() {

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    private val viewModel: DashboardViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database
                .noteDao()
        )
    }

    lateinit var note: Note

    private var _binding: AddNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = AddNoteBinding.inflate(inflater, container, false)
//        Cancel button
        binding.cancelButton.setOnClickListener {
            val action = AddNoteDirections.actionNavigationAddnoteToNavigationDashboard()
            findNavController().navigate(action)
        }
//        check button
        binding.checkButton.setOnClickListener {
            addNewItem()
        }
//        image button
        binding.imageButton.setOnClickListener {
            activity?.takeIf { !it.isFinishing && !it.isDestroyed }?.let { activity ->
                MyDatePickerDialog(activity, ::datePick).show()
            }
        }
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.dateView.text.toString(),
            binding.inputNote.text.toString()
        )
    }

    private fun bind(note: Note) {
        binding.dateView.setText(note.note, TextView.BufferType.SPANNABLE)
        binding.inputNote.setText(note.date, TextView.BufferType.SPANNABLE)
        binding.checkButton.setOnClickListener { updateItem() }

    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                this.navigationArgs.itemId,
                this.binding.dateView.text.toString(),
                this.binding.inputNote.text.toString()
            )
            val action = AddNoteDirections.actionNavigationAddnoteToNavigationDashboard()
            findNavController().navigate(action)
        }
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(
                binding.dateView.text.toString(),
                binding.inputNote.text.toString(),
            )
            val action = AddNoteDirections.actionNavigationAddnoteToNavigationDashboard()
            findNavController().navigate(action)
        }
    }

    fun datePick(year: Int, month: Int, day: Int){
        val nyear = year.toString()
        val nmonth = month.toString()
        val nday = day.toString()
        val date = "$nyear-$nmonth-$nday"
        binding.dateView.text = date
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                note = selectedItem
                bind(note)
            }
        } else {
            binding.checkButton.setOnClickListener {
                if(isEntryValid())
                    addNewItem()
                else
                    Toast.makeText(context, "invalid input!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}
