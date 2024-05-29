package com.example.todolist.ui.dashboard

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.databinding.FragmentDashboardBinding
import com.example.todolist.databinding.ItemListItemBinding

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
        val nyear = year
        val nmonth = (month + 1)
        val nday = day
        var date = ""
        date = if(nmonth < 10 && nday < 10){
            "$nyear-0$nmonth-0$nday"
        }else if(nday < 10){
            "$nyear-$nmonth-0$nday"
        }else if(nmonth < 10){
            "$nyear-0$nmonth-$nday"
        }else{
            "$nyear-$nmonth-$nday"
        }
        binding.dateView.text = date
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}