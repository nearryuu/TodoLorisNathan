package com.lorisnath.todo.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lorisnath.todo.detail.ui.theme.TodoLorisNathanTheme

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
                    Detail()
                }
            }
        }
    }
}

@Composable
fun Detail( modifier: Modifier = Modifier) {
    Text(
        text = "Task Detail",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TodoLorisNathanTheme {
        Detail()
    }
}