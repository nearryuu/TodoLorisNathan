package com.lorisnath.todo.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lorisnath.todo.detail.ui.theme.TodoLorisNathanTheme
import com.lorisnath.todo.list.Task
import java.util.UUID

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoLorisNathanTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Detail(onValidate =  {newTask ->
                        intent.putExtra("task", newTask)
                        setResult(RESULT_OK, intent)
                        finish()
                    })
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detail( modifier: Modifier = Modifier, onValidate: (Task) -> Unit) {
    var task by remember { mutableStateOf(Task(id = UUID.randomUUID().toString(), title = "New Task !")) }
    Column {
        modifier.padding(16.dp)
        Arrangement.spacedBy(16.dp)
    Text(
         text = "Task Detail",
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge
    )
        OutlinedTextField(
            value = task.title,
            modifier = modifier,
            onValueChange = {task = task.copy(title = it)}
        )
        OutlinedTextField(
            value = task.description,
            modifier = modifier,
            onValueChange = {task = task.copy(description = it)}
        )

        Button(onClick = {
            onValidate(task)

        }) {
            Text(text = "Confirm")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TodoLorisNathanTheme {
        Detail(onValidate = {})
    }
}