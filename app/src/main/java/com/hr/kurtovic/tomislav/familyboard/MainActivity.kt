package com.hr.kurtovic.tomislav.familyboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.hr.kurtovic.tomislav.familyboard.auth.AuthFragment
import com.hr.kurtovic.tomislav.familyboard.main_board.MessageInputFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val sharedViewModel: SharedViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                showMainBoard()
            } else {
                showLoginScreen()
            }
        }
    }

    fun showLoginScreen() {
        replaceFragment(AuthFragment.newInstance())
    }

    fun showMainBoard() {
        replaceFragment(FragmentHolder.newInstance())
    }

    fun showMessageInputScreen() {
        replaceFragment(MessageInputFragment.newInstance(), addToBackStack = true)
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val tag = fragment::class.java.simpleName
        val currentFragment = supportFragmentManager.findFragmentByTag(tag)
        if (currentFragment?.isVisible == true) {
            return;
        }
        supportFragmentManager.commit {
            if (addToBackStack) {
                addToBackStack(tag)
            }
            replace(R.id.fragmentContainer, fragment, tag)
        }
    }
}
