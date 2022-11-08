package com.dicoding.submissionintermediatedicoding.ui.auth

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submissionintermediatedicoding.R
import com.dicoding.submissionintermediatedicoding.data.preferences.UserLoginPreferences
import com.dicoding.submissionintermediatedicoding.databinding.FragmentRegisterBinding
import com.dicoding.submissionintermediatedicoding.utils.Status
import com.dicoding.submissionintermediatedicoding.utils.progress.ProgressDialog
import com.dicoding.submissionintermediatedicoding.viewmodel.AuthViewModel
import com.shashank.sony.fancytoastlib.FancyToast

class RegisterFragment : Fragment() {
    private var registerFragmentBinding: FragmentRegisterBinding? = null
    private lateinit var authViewModel: AuthViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var usrLoginPref: UserLoginPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerFragmentBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        return registerFragmentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initVM()
        initPref()
    }

    private fun initPref(){
        usrLoginPref = UserLoginPreferences(requireContext())
    }

    override fun onDetach() {
        super.onDetach()
        registerFragmentBinding = null
    }

    private fun initView(){
        progressDialog = ProgressDialog(requireActivity())
        registerFragmentBinding?.apply {
            buttonSignUp.setOnClickListener{
                validateAndRegister()
            }
        }
    }

    private fun initVM(){
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        authViewModel.usrRegister.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.startProgressDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissProgressDialog()
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle(context?.getString(R.string.register_success))
                        setMessage("${context?.getString(R.string.registered_as)} ${usrLoginPref.getName()}!\n" +
                                "${context?.getString(R.string.please_log)}")
                        setPositiveButton("Ok") { _, _ ->
                            (activity as AuthActivity).chooseTab(0)
                        }
                        create()
                        setCancelable(false)
                        show()
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismissProgressDialog()
                    showMessage(context?.getString(R.string.email_already))
                }
                Status.EMPTY -> {
                    progressDialog.dismissProgressDialog()
                }
            }
        }
    }

    private fun doRegister(){
        val username = registerFragmentBinding?.inputName?.text.toString().trim()
        val usrEmail = registerFragmentBinding?.inputEmail?.text.toString().trim()
        val usrPass = registerFragmentBinding?.inputPassword?.text.toString().trim()

        authViewModel.doRegister(username, usrEmail, usrPass)
        usrLoginPref.setName(username)
    }

    private fun validateAndRegister() {
        when {
            registerFragmentBinding?.inputEmail?.text!!.isBlank() -> {
                registerFragmentBinding?.inputEmail!!.error = context?.getString(R.string.email_is_required)
                return
            }
            registerFragmentBinding!!.inputPassword.text!!.isBlank() || registerFragmentBinding!!.inputPassword.length() < 6 -> {
                registerFragmentBinding!!.inputPassword.error = context?.getString(R.string.password_is_required)
                return
            }
        }
        //doRegister
        doRegister()

    }

    private fun showMessage(message: String?){
        FancyToast.makeText(requireContext(), message, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
    }

}