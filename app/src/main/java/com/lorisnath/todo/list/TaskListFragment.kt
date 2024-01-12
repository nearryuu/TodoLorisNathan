package com.lorisnath.todo.list

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lorisnath.todo.R
import com.lorisnath.todo.data.Api
import com.lorisnath.todo.databinding.FragmentTaskListBinding
import com.lorisnath.todo.detail.DetailActivity
import com.lorisnath.todo.user.UserActivity
import kotlinx.coroutines.launch
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

    private val viewModel: TasksListViewModel by viewModels()


    val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            viewModel.remove(task)
        }
        override fun onClickEdit(task: Task) {
            var intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("previousTask", task)
            editTask.launch(intent)
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
        viewModel.add(task!!)
    }

    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // dans cette callback on récupèrera la task et on remplacera au bon endroit
        val task = result.data?.getSerializableExtra("task") as Task?
        viewModel.update(task!!)
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

    override fun onResume() {
        super.onResume()
        viewModel.refresh() // on demande de rafraîchir les données sans attendre le retour directement
        lifecycleScope.launch {
            // Ici on ne va pas gérer les cas d'erreur donc on force le crash avec "!!"
            val user = Api.userWebService.fetchUser().body()!!
            _binding?.UserTextView?.text  = user.name
            var imageView = _binding!!.imageView;
            imageView.load(user.avatar) {
                error(R.drawable.ic_launcher_background) // image par défaut en cas d'erreur
            }
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = _binding?.recycler
        recyclerView?.adapter = adapter

        /*
        val array : Array<Task>? = savedInstanceState?.getSerializable("tasks") as? Array<Task>
        if (array !=null ) taskList = array.asList()
        adapter.submitList(taskList)
         */
        val button : FloatingActionButton? = _binding?.floatingActionButton
        button?.setOnClickListener {
            var intent = Intent(context, DetailActivity::class.java)
            createTask.launch(intent)

        }

        _binding!!.imageView.setOnClickListener {
            var intent = Intent(context, UserActivity::class.java)
            intent.putExtra("name", _binding?.UserTextView?.text)
            startActivity(intent)
        }

        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est exécutée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter

                adapter.submitList(newList)
            }
        }


    }
    /*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val array = taskList.toTypedArray()
        outState.putSerializable("tasks", array)
    }

     */

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