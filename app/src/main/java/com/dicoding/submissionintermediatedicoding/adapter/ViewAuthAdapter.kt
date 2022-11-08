package com.dicoding.submissionintermediatedicoding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.submissionintermediatedicoding.ui.auth.LoginFragment
import com.dicoding.submissionintermediatedicoding.ui.auth.RegisterFragment

class ViewAuthAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            RegisterFragment()
        } else LoginFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}
