package com.example.khajakhoj

import com.example.khajakhoj.repository.AuthRepositoryImpl
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LoginSignupUnitTest {
    @Mock
    private lateinit var mockAuth: FirebaseAuth

    @Mock
    private lateinit var mockTask: Task<AuthResult>

    private lateinit var authRepository: AuthRepositoryImpl

    @Captor
    private lateinit var captor: ArgumentCaptor<OnCompleteListener<AuthResult>>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        authRepository = AuthRepositoryImpl(mockAuth)
    }

    @Test
    fun testRegister_Successful() {
        val email = "test@example.com"
        val password = "testPassword"
        var expectedResult = "Initial Value" // Define the initial value

        `when`(mockTask.isSuccessful).thenReturn(true)
        `when`(mockAuth.createUserWithEmailAndPassword(any(), any()))
            .thenReturn(mockTask)

        val callback = { success: Boolean, message: String? ->
            expectedResult = message ?: "Callback message is null"
        }

        authRepository.signup(email, password, callback)

        verify(mockTask).addOnCompleteListener(captor.capture())
        captor.value.onComplete(mockTask)

        // Assert the result
        assertEquals("Signup successful", expectedResult)
    }

    @Test
    fun testLogin_Successful() {
        val email = "test@example.com"
        val password = "testPassword"
        var expectedResult = "Initial Value" // Define the initial value

        `when`(mockTask.isSuccessful).thenReturn(true)
        `when`(mockAuth.signInWithEmailAndPassword(any(), any()))
            .thenReturn(mockTask)

        val callback = { success: Boolean, message: String? ->
            expectedResult = message ?: "Callback message is null"
        }

        authRepository.login(email, password, callback)

        verify(mockTask).addOnCompleteListener(captor.capture())
        captor.value.onComplete(mockTask)

        // Assert the result
        assertEquals("Login successful", expectedResult)
    }
}