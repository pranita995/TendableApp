package com.example.tendableapp.ui.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.tendableapp.data.LoginRepository
import com.example.tendableapp.data.Result

import com.example.tendableapp.R
import com.example.tendableapp.data.Response.BaseResponse
import com.example.tendableapp.data.request.LoginRequest
import kotlinx.coroutines.launch
import java.io.EOFException

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess
    /*fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }*/

    fun loginUser(email: String, pwd: String) {

       // loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            val loginRequest = LoginRequest(email, pwd)
            try {
                val response = loginRepository.loginUser(loginRequest)

                when (response!!.code()) {
                    200 -> {
                        println("Registration successful")
                        _registrationSuccess.postValue(true)  // Notify success
                    }
                    400 -> {
                        println("Registration unsuccessful: Missing fields in the JSON")
                    }
                    401 -> {
                        println("Registration unsuccessful: User already exists")
                    }
                    else -> {
                        println("Unexpected response code: ${response.code()}")
                    }
                }
            } catch (e: EOFException) {
                println("Server returned an empty response: ${e.localizedMessage}")
            } catch (e: Exception) {
                println("Network or server error: ${e.localizedMessage}")
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}