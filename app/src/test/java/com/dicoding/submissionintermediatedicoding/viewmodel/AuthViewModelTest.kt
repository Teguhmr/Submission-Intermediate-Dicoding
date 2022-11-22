package com.dicoding.submissionintermediatedicoding.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissionintermediatedicoding.data.auth.UserLoginResult
import com.dicoding.submissionintermediatedicoding.data.auth.UserRegisterResponse
import com.dicoding.submissionintermediatedicoding.data.repository.AuthRepository
import com.dicoding.submissionintermediatedicoding.utils.Resource
import com.dicoding.submissionintermediatedicoding.utils.Status
import com.dicoding.submissionintermediatedicoding.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest{

    @get:Rule
    var instantExecutorRle = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var authVM : AuthViewModel

    @Before
    fun setUp() {
        authVM = AuthViewModel(authRepository)
    }

    @Test
    fun `when get usrLogin from viewModel should return LiveData UserLoginResult`() {
        val expectedData = MutableLiveData<Resource<UserLoginResult>>()
        val dummyUserResult = UserLoginResult(
            "dummyUser",
            "dummyPassword",
            "dummyToken"
        )
        expectedData.value = Resource.success(dummyUserResult)

        Mockito.`when`(authRepository.usrLogin).thenReturn(expectedData)

        val actualData = authVM.usrLogin.getOrAwaitValue()

        Mockito.verify(authRepository).usrLogin
        assertTrue(actualData.status == Status.SUCCESS)
        assertEquals(actualData, expectedData.value)
    }

    @Test
    fun `when get message usrLogin from viewModel should return LiveData String`() {
        val dummyMessage = "dummyMessage"

        val expectedData = MutableLiveData<Resource<UserLoginResult>>()
        expectedData.value = Resource.error(dummyMessage, null)

        Mockito.`when`(authRepository.usrLogin).thenReturn(expectedData)

        val actualData = authVM.usrLogin.getOrAwaitValue()

        Mockito.verify(authRepository).usrLogin
        assertTrue(actualData.status == Status.ERROR)
        assertEquals(actualData, expectedData.value)
    }

    @Test
    fun `when get isLoading usrLogin from viewModel should return LiveData Boolean`() {
        val expectedData = MutableLiveData<Resource<UserLoginResult>>()
        expectedData.value = Resource.loading(null)

        Mockito.`when`(authRepository.usrLogin).thenReturn(expectedData)

        val actualData = authVM.usrLogin.getOrAwaitValue()

        Mockito.verify(authRepository).usrLogin
        assertTrue(actualData.status == Status.LOADING)
        assertEquals(actualData, expectedData.value)

    }


    @Test
    fun `verify doLogin function in view model works`() {
        val dummyEmail = "someone@gmail.com"
        val dummyPassword = "someonePassHere"
        val dummyLoginResult = UserLoginResult("someone@gmail.com", "someonePassHere", "someId")

        val expectedData = MutableLiveData<Resource<UserLoginResult>>()
        expectedData.value = Resource.success(dummyLoginResult)

        authVM.doLogin(dummyEmail, dummyPassword)

        Mockito.`when`(authRepository.usrLogin).thenReturn(expectedData)

        val actualData = authVM.usrLogin.getOrAwaitValue()

        Mockito.verify(authRepository).doLogin(dummyEmail, dummyPassword)
        assertTrue(actualData.status == Status.SUCCESS)
        assertEquals(actualData, expectedData.value)
    }

    @Test
    fun `verify doRegister function in view model works`() {
        val dummyName = "someone"
        val dummyEmail = "someone@gmail.com"
        val dummyPassword = "someonePassHere"
        val dummyRegisterResponse =
            UserRegisterResponse(
                false,
                "someonePassHere",)

        val expectedData = MutableLiveData<Resource<UserRegisterResponse>>()
        expectedData.value = Resource.success(dummyRegisterResponse)

        authVM.doRegister(dummyName, dummyEmail, dummyPassword)

        Mockito.`when`(authRepository.usrRegister).thenReturn(expectedData)

        val actualData = authVM.usrRegister.getOrAwaitValue()

        Mockito.verify(authRepository).doRegister(dummyName, dummyEmail, dummyPassword)
        assertTrue(actualData.status == Status.SUCCESS)
        assertEquals(actualData, expectedData.value)

    }
}