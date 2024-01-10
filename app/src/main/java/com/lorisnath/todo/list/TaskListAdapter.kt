package com.lorisnath.todo.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lorisnath.todo.R


object MyItemsDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem.id == newItem.id// comparaison: est-ce la même "entité" ? => même id?
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem == newItem // comparaison: est-ce le même "contenu" ? => mêmes valeurs? (avec data class: simple égalité)
    }
}

class TaskListAdapter(val listener: TaskListListener) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(MyItemsDiffCallback) {

    // on utilise `inner` ici afin d'avoir accès aux propriétés de l'adapter directement
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var textView : TextView = itemView.findViewById(R.id.task_title)
        private var descView : TextView = itemView.findViewById(R.id.task_description)
        private var deleteButton : ImageButton = itemView.findViewById(R.id.imageButton2)
        private var editButton : ImageButton = itemView.findViewById(R.id.editButton)


        fun bind(task: Task) {
            // on affichera les données ici
            textView.text = task.title
            descView.text = task.description
            deleteButton.setOnClickListener { listener.onClickDelete(task) }
            editButton.setOnClickListener { listener.onClickEdit(task) }
            itemView.setOnLongClickListener {listener.onClickTask(task.description); true}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}