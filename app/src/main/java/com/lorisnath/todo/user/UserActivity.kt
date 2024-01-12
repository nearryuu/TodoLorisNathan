package com.lorisnath.todo.user

import android.Manifest
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.lorisnath.todo.data.Api
import com.lorisnath.todo.list.Task
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserActivity : AppCompatActivity() {

    private fun Bitmap.toRequestBody(): MultipartBody.Part {
        val tmpFile = File.createTempFile("avatar", "jpg")
        tmpFile.outputStream().use { // *use*: open et close automatiquement
            this.compress(Bitmap.CompressFormat.JPEG, 100, it) // *this* est le bitmap ici
        }
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = tmpFile.readBytes().toRequestBody()
        )
    }
    private fun Uri.toRequestBody(): MultipartBody.Part {
        val fileInputStream = contentResolver.openInputStream(this)!!
        val fileBody = fileInputStream.readBytes().toRequestBody()
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = fileBody
        )
    }

    private val userViewModel : UserViewModel by viewModels()

    private val capturedUri by lazy {
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
    }
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var name = intent.getSerializableExtra("name") as String
        setContent {

            var bitmap: Bitmap? by remember { mutableStateOf(null) }
            var newName: String by remember { mutableStateOf(name) }
            var uri: Uri? by remember { mutableStateOf(null) }
            val composeScope = rememberCoroutineScope()

            fun send(uri: Uri?){
                composeScope.launch {
                    userViewModel.updateAvatar(uri!!.toRequestBody())
                }
            }

            fun sendName(name: String)
            {
                composeScope.launch {
                    var userUpdate = UserUpdate(name)
                    userViewModel.update(userUpdate)
                }
            }

            val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {success ->
                if (success) {
                    uri = capturedUri
                    send(uri)
                }
            }

            val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                uri = it
                send(uri)
            }
            val getPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }



            Column {
                AsyncImage(
                    modifier = Modifier.fillMaxHeight(.2f),
                    model = bitmap ?: uri,
                    contentDescription = null
                )
                Button(
                    onClick = { takePicture.launch(capturedUri) },
                    content = { Text("Take picture") }
                )
                Button(
                    onClick = {getPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)},
                    content = { Text("Pick photo") }
                )

                OutlinedTextField(
                    value = newName,
                    onValueChange = {newName = it}
                )

                Button(onClick = {
                    sendName(newName);
                    //finish()

                }) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}






