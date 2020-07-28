package com.hr.kurtovic.tomislav.familyboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.hr.kurtovic.tomislav.familyboard.family_list.FamilyListFragment
import com.hr.kurtovic.tomislav.familyboard.main_board.MainBoardFragment
import com.hr.kurtovic.tomislav.familyboard.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_board.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentHolder : Fragment() {

    private val fragmentHolderViewModel: FragmentHolderViewModel by viewModel()

    companion object {
        fun newInstance() = FragmentHolder()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?
        , savedInstanceState: Bundle?
    )
            : View? = inflater.inflate(R.layout.activity_board, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHolderViewModel.state.observe(viewLifecycleOwner, Observer { init(it!!) })
        configureBottomNavigation()
    }

    private fun configureBottomNavigation() {
        //TODO replace recreation of fragments every time the item changes
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.room_list_item -> {
                    showListRoom()
                    true
                }
                R.id.main_chat_item -> {
                    showFamilyBoard()
                    true
                }
                R.id.profile_item -> {
                    showProfile()
                    true
                }
                else -> false
            }
        }
    }

    private fun init(state: State) {

        if (state.loading) {
            return
        } else {
            fragment_holder_progress.isVisible = false
        }

        if (state.noSavedFamilies) {
            bottom_navigation.selectedItemId = R.id.room_list_item
            return
        }

        if (state.savedFamilyName!!.isEmpty()) {
            bottom_navigation.selectedItemId = R.id.profile_item
        } else {
            bottom_navigation.selectedItemId = R.id.main_chat_item
        }
    }


    private fun showListRoom() {
        replaceFragment(FamilyListFragment.newInstance())
    }

    private fun showFamilyBoard() {
        replaceFragment(MainBoardFragment.newInstance())
    }

    private fun showProfile() {
        replaceFragment(ProfileFragment.newInstance())
    }


    private fun replaceFragment(fragment: Fragment) {
        val tag = fragment::class.java.simpleName
        parentFragmentManager.commit {
            this.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            replace(R.id.main_board_fragment_container, fragment, tag)
        }
    }
}
