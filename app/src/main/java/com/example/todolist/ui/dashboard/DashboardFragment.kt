package com.example.todolist.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private val viewModel: DashboardViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.noteDao()
        )
    }
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DashboardAdapter {
            val action = DashboardFragmentDirections.actionNavigationDashboardToItemDetailFragment2(
                it.id
            )
            this.findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
        binding.floatingActionButton.setOnClickListener {
            val action = DashboardFragmentDirections.actionNavigationDashboardToNavigationAddnote(
                getString(R.string.addnote)
            )
            this.findNavController().navigate(action)
        }

        binding.imageButton.setOnClickListener {
            activity?.takeIf { !it.isFinishing && !it.isDestroyed }?.let { activity ->
                MyDatePickerDialog(activity, ::datePick).show()
            }
        }

        binding.checkSelectButton.setOnClickListener {
            val test = viewModel.findNote(binding.dateView.text.toString())
            test.observe(this.viewLifecycleOwner) { items ->
                items.let {
                    adapter.submitList(it)
                }
            }
        }
    }

    fun datePick(year: Int, month: Int, day: Int){
        val nyear = year.toString()
        val nmonth = (month + 1).toString()
        val nday = day.toString()
        val date = "$nyear-$nmonth-$nday"
        binding.dateView.text = date
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}