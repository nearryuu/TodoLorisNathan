package com.lorisnath.todo.user

import androidx.lifecycle.ViewModel
import com.lorisnath.todo.data.Api
import okhttp3.MultipartBody

class UserViewModel : ViewModel() {

    suspend fun updateAvatar(request : MultipartBody.Part) {
        Api.userWebService.updateAvatar(request)
    }

    suspend fun update(user : UserUpdate) {
        Api.userWebService.update(user)
    }
}