package com.dicoding.submissionintermediatedicoding

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.submissionintermediatedicoding.data.preferences.UserLoginPreferences
import com.dicoding.submissionintermediatedicoding.databinding.ActivityMainBinding
import com.dicoding.submissionintermediatedicoding.ui.auth.AuthActivity
import com.dicoding.submissionintermediatedicoding.ui.home.HomeFragment
import com.dicoding.submissionintermediatedicoding.viewmodel.StoryPagerViewModel
import com.dicoding.submissionintermediatedicoding.viewmodel.ViewModelStoryFactory

class MainActivity : AppCompatActivity() {
    private lateinit var userLoginPref: UserLoginPreferences
    private var mainActivityMainBinding: ActivityMainBinding? = null
    private var homeFragment: HomeFragment? = null
    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userLoginPref = UserLoginPreferences(this)
        token = "Bearer ${userLoginPref.getLoginData().token}"
        homeFragment = HomeFragment()

        moveToFragment(homeFragment!!)
    }

    private fun moveToFragment(fragment: Fragment){
        this.supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivityMainBinding = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logoutMenu -> {
                doLogout()
                true
            }
            R.id.settingLanguage -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.home_up -> {
                homeFragment!!.scrollToPosition()
                true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }

    private fun doLogout(){
        userLoginPref.logout()
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    fun getStoryViewModel(): StoryPagerViewModel {
        val factory = ViewModelStoryFactory(this, null)
        val viewModel: StoryPagerViewModel by viewModels {
            factory
        }
        return viewModel
    }
}