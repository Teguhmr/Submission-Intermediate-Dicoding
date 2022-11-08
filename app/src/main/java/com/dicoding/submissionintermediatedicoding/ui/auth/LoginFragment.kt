package com.dicoding.submissionintermediatedicoding.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submissionintermediatedicoding.MainActivity
import com.dicoding.submissionintermediatedicoding.R
import com.dicoding.submissionintermediatedicoding.data.auth.UserSession
import com.dicoding.submissionintermediatedicoding.data.preferences.UserLoginPreferences
import com.dicoding.submissionintermediatedicoding.databinding.FragmentLoginBinding
import com.dicoding.submissionintermediatedicoding.utils.Status
import com.dicoding.submissionintermediatedicoding.utils.progress.ProgressDialog
import com.dicoding.submissionintermediatedicoding.viewmodel.AuthViewModel
import com.shashank.sony.fancytoastlib.FancyToast

class LoginFragment : Fragment() {
    private var loginFragmentBinding: FragmentLoginBinding? = null
    private lateinit var authViewModel: AuthViewModel
    private lateinit var usrLoginPref: UserLoginPreferences
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginFragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return loginFragmentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initPref()
        initVM()
    }

    private fun initPref(){
        usrLoginPref = UserLoginPreferences(requireContext())
    }

    private fun initView(){
        progressDialog = ProgressDialog(requireActivity())
        loginFragmentBinding?.apply {
            buttonSignIn.setOnClickListener{
                validateAndLogin()
            }
        }
    }

    private fun doLogin(){
        val usrEmail = loginFragmentBinding?.inputEmail?.text.toString().trim()
        val usrPass = loginFragmentBinding?.inputPassword?.text.toString().trim()
        authViewModel.apply {
            doLogin(usrEmail, usrPass)
            usrLogin.observe(viewLifecycleOwner){
                if (it.data != null){
                    //save the login session

                    val currentUser = UserSession(
                        it.data.name,
                        it.data.token,
                        it.data.userId,
                        true
                    )

                    Log.d("LoginFragment", "doLogin: $currentUser")
                    usrLoginPref.setUserLogin(currentUser)
                    showMessage("${context?.getString(R.string.welcome)}, ${it.data.name}!")
                }

            }
        }

    }

    private fun initVM(){
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        authViewModel.usrLogin.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.startProgressDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissProgressDialog()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                Status.ERROR -> {
                    progressDialog.dismissProgressDialog()
                    showMessage(context?.getString(R.string.err_email_or_password))

                }
                Status.EMPTY -> {
                    progressDialog.dismissProgressDialog()
                }
            }
        }
    }

    private fun validateAndLogin() {
        when {
            loginFragmentBinding?.inputEmail?.text!!.isBlank() -> {
                loginFragmentBinding?.inputEmail?.error = context?.getString(R.string.email_is_required)
                return
            }
            loginFragmentBinding?.inputPassword?.text!!.isBlank() || loginFragmentBinding!!.inputPassword.length() < 6 -> {
                loginFragmentBinding?.inputPassword!!.error = context?.getString(R.string.password_is_required)
                return
            }
        }
        //doLogin
        doLogin()

    }

    private fun showMessage(message: String?){
        FancyToast.makeText(requireContext(), message, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
    }

    override fun onDetach() {
        super.onDetach()
        loginFragmentBinding = null
    }

}