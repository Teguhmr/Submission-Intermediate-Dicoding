package com.dicoding.submissionintermediatedicoding.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionintermediatedicoding.data.auth.UserLoginResponse
import com.dicoding.submissionintermediatedicoding.data.auth.UserLoginResult
import com.dicoding.submissionintermediatedicoding.data.auth.UserRegisterResponse
import com.dicoding.submissionintermediatedicoding.data.remote.api.RetrofitConfig
import com.dicoding.submissionintermediatedicoding.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel(){
    private val _usrLogin = MutableLiveData<Resource<UserLoginResult>>()
    val usrLogin: LiveData<Resource<UserLoginResult>> = _usrLogin

    private val _usrRegister = MutableLiveData<Resource<UserRegisterResponse>>()
    val usrRegister: LiveData<Resource<UserRegisterResponse>> = _usrRegister

//    private val _message = MutableLiveData<String>()
//
//    val loading = MutableLiveData<Resource<Status>>()
//    val loadingRegister = MutableLiveData<ApiState>()

    fun doLogin(email:String, password:String){
//        loading.postValue(ApiState.Loading)
        _usrLogin.value = Resource.loading(null)
        RetrofitConfig.getApiService().userLogin(email, password)
            .enqueue(object : Callback<UserLoginResponse> {
                override fun onResponse(
                    call: Call<UserLoginResponse>,
                    response: Response<UserLoginResponse>
                ) {
                    if (response.isSuccessful){
//                        loading.postValue(ApiState.Success)
                        _usrLogin.value = Resource.success(response.body()?.loginResult)
                    } else {
//                        loading.postValue(ApiState.Failure)
                        _usrLogin.value = Resource.error(response.message(), null)
                    }
                }

                override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
//                    _message.value = t.message
                    _usrLogin.value = Resource.error(t.message.toString(), null)
                }

            })
    }

    fun doRegister(name: String, email: String, password: String){
//        loadingRegister.postValue(ApiState.Loading)
        _usrRegister.value = Resource.loading(null)
        RetrofitConfig.getApiService().userRegister(name,email,password)
            .enqueue(object : Callback<UserRegisterResponse>{
                override fun onResponse(
                    call: Call<UserRegisterResponse>,
                    response: Response<UserRegisterResponse>
                ) {
                    if (response.isSuccessful){
                        _usrRegister.value = Resource.success(response.body())
                    } else {
                        _usrRegister.value = Resource.error(response.message(), null)
                    }
                }

                override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                    _usrRegister.value = Resource.error(t.message.toString(), null)

                }

            })
    }
}