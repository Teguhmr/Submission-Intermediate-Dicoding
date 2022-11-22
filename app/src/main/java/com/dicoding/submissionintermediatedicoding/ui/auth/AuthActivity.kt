package com.dicoding.submissionintermediatedicoding.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.submissionintermediatedicoding.adapter.ViewAuthAdapter
import com.dicoding.submissionintermediatedicoding.data.remote.api.RetrofitConfig
import com.dicoding.submissionintermediatedicoding.databinding.ActivityAuthBinding
import com.dicoding.submissionintermediatedicoding.viewmodel.AuthViewModel
import com.dicoding.submissionintermediatedicoding.viewmodel.ViewModelStoryFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        binding.viewpagerSignInUp.adapter = ViewAuthAdapter(this)
        val tabLayoutMediator = TabLayoutMediator(
            binding.tabLayoutLoginRegister, binding.viewpagerSignInUp
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    val txtLogin = "Login"
                    tab.text = txtLogin
                }
                1 -> {
                    val txtRegister = "Register"
                    tab.text = txtRegister
                }
            }

            binding.viewpagerSignInUp.isUserInputEnabled = false
        }
        tabLayoutMediator.attach()

    }

    fun chooseTab(position: Int) {
        binding.viewpagerSignInUp.currentItem = position
    }

    fun getAuthViewModel(): AuthViewModel {
        val viewModel: AuthViewModel by viewModels {
            ViewModelStoryFactory(
                this,
                RetrofitConfig.getApiService()
            )
        }
        return viewModel
    }
}