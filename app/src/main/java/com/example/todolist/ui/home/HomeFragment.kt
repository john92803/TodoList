package com.example.todolist.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentHomeBinding
import com.example.todolist.ui.dashboard.DashboardViewModel
import com.example.todolist.ui.dashboard.InventoryApplication
import com.example.todolist.ui.dashboard.InventoryViewModelFactory
import com.example.todolist.ui.data.NoteDao
import kotlinx.coroutines.flow.count
import java.time.LocalDate

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.noteDao()
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val today = viewModel.findNoteByCount(LocalDate.now().toString()).asLiveData()

        today.observe(viewLifecycleOwner) {
            if(it.isEmpty())
                binding.textView.text = "今天沒有任何事情了喔!!"
            else
                binding.textView.text = "今天還有${it.size}件事還沒做完喔!!"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
