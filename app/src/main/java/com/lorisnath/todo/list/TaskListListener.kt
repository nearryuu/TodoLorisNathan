package com.lorisnath.todo.list

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)

    fun onClickTask(desc: String)
}