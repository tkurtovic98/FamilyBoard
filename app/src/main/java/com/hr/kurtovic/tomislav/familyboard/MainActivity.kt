package com.hr.kurtovic.tomislav.familyboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.hr.kurtovic.tomislav.familyboard.auth.AuthFragment
import com.hr.kurtovic.tomislav.familyboard.auth.AuthManager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val firebaseAuth: AuthManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            if (firebaseAuth.isUserLogged()) {
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
        replaceFragment(BoardFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        val tag = fragment::class.java.simpleName
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment, tag)
        }
    }
}
