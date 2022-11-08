package com.dicoding.submissionintermediatedicoding.data.auth

data class UserSession(
    val name: String,
    val token: String,
    val userId: String,
    val isLogin: Boolean
)