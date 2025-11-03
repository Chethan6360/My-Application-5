package com.example.myapplication3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(private val auth: FirebaseAuth) : ViewModel() {

    private val _user = MutableStateFlow(auth.currentUser)
    val user: StateFlow<com.google.firebase.auth.FirebaseUser?> = _user

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _user.value = auth.currentUser
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _user.value = auth.currentUser
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun logout() {
        auth.signOut()
        _user.value = null
    }
}

class AuthViewModelFactory(private val auth: FirebaseAuth) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(auth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}