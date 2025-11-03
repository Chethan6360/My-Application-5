package com.example.myapplication3

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.google.android.gms.tasks.Tasks
import org.junit.Assert.assertEquals
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var authViewModel: AuthViewModel
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockUser: FirebaseUser
    private lateinit var mockAuthResult: AuthResult

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockAuth = mock()
        mockUser = mock()
        mockAuthResult = mock()
        authViewModel = AuthViewModel(mockAuth)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success`() = runTest {
        whenever(mockAuth.signInWithEmailAndPassword(any(), any())).thenReturn(Tasks.forResult(mockAuthResult))
        whenever(mockAuth.currentUser).thenReturn(mockUser)

        authViewModel.login("test@example.com", "password")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(mockUser, authViewModel.user.value)
    }

    @Test
    fun `logout success`() = runTest {
        authViewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(null, authViewModel.user.value)
    }
}