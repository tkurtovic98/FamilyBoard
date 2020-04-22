package com.hr.kurtovic.tomislav.familyboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.hr.kurtovic.tomislav.familyboard.auth.AuthFragment
import com.hr.kurtovic.tomislav.familyboard.main_board.MessageInputFragment

class MainActivity : AppCompatActivity() {

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
        replaceFragment(MessageInputFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        val tag = fragment::class.java.simpleName
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment, tag)
        }
    }
}
