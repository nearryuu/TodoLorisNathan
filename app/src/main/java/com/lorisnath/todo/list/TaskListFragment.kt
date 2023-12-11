package com.lorisnath.todo.list

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lorisnath.todo.R
import com.lorisnath.todo.databinding.FragmentTaskListBinding
import com.lorisnath.todo.detail.DetailActivity
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskListFragment : Fragment() {
    //private var taskList = listOf("Task 1", "Task 2", "Task 3")
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    private val adapter = TaskListAdapter()
    private var _binding: FragmentTaskListBinding? = null
    //val intent = Intent(context, DetailActivity::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        //var button = view?.findViewById(R.id.floatingActionButton) as FloatingActionButton

/*
        button.setOnClickListener{

            val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList = taskList + newTask
            adapter.currentList = taskList
            adapter.notifyDataSetChanged()
            */
       // }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        // adapter.currentList = taskList
        // Inflate the layout for this fragment
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = _binding?.recycler
        recyclerView?.adapter = adapter
        adapter.onClickDelete = { task ->
            /*
            taskList = taskList.subList(0, taskList.indexOf(task)-1) + taskList.subList(taskList.indexOf(task)+ 1, taskList.size - 1)
            adapter.submitList(taskList)
             */
            taskList = taskList.minus(task)
            adapter.submitList(taskList)
        }
        adapter.submitList(taskList)
        val button : FloatingActionButton? = _binding?.floatingActionButton
        button?.setOnClickListener {
            //startActivity(intent)
            val newTask =
                Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList = taskList + newTask
            adapter.submitList(taskList)
        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}