package com.lorisnath.todo.list

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
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


    val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            taskList = taskList.minus(task)
            adapter.submitList(taskList)
        }
        override fun onClickEdit(task: Task) {
            var intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("previousTask", task)
            editTask.launch(intent)
            adapter.submitList(taskList)
        }

        override fun onClickTask(desc: String) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, desc)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }
    private val adapter = TaskListAdapter(adapterListener)
    private var _binding: FragmentTaskListBinding? = null
    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // dans cette callback on récupèrera la task et on l'ajoutera à la liste
        val task = result.data?.getSerializableExtra("task") as Task?
        taskList = taskList + task!!
        adapter.submitList(taskList)
    }

    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // dans cette callback on récupèrera la task et on remplacera au bon endroit
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null) {
            taskList = taskList.map { if (it.id == task.id) task else it }
        }
        adapter.submitList(taskList)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

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

        val array : Array<Task>? = savedInstanceState?.getSerializable("tasks") as? Array<Task>
        if (array !=null ) taskList = array.asList()
        adapter.submitList(taskList)
        val button : FloatingActionButton? = _binding?.floatingActionButton
        button?.setOnClickListener {
            var intent = Intent(context, DetailActivity::class.java)
            createTask.launch(intent)

        }


    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val array = taskList.toTypedArray()
        outState.putSerializable("tasks", array)
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