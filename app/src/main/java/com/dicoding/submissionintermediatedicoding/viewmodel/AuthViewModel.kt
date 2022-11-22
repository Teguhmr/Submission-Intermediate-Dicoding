package com.dicoding.submissionintermediatedicoding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionintermediatedicoding.data.auth.UserLoginResult
import com.dicoding.submissionintermediatedicoding.data.auth.UserRegisterResponse
import com.dicoding.submissionintermediatedicoding.data.repository.AuthRepository
import com.dicoding.submissionintermediatedicoding.utils.Resource

class AuthViewModel(private val storyRepository: AuthRepository) : ViewModel(){
    val usrLogin: LiveData<Resource<UserLoginResult>> by lazy {
        storyRepository.usrLogin
    }
    val usrRegister: LiveData<Resource<UserRegisterResponse>> by lazy {
        storyRepository.usrRegister
    }

    fun doLogin(email:String, password:String) = storyRepository.doLogin(email, password)

    fun doRegister(name: String, email: String, password: String) = storyRepository.doRegister(name, email, password)

}