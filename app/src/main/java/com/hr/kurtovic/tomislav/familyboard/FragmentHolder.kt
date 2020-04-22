package com.hr.kurtovic.tomislav.familyboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyService
import com.hr.kurtovic.tomislav.familyboard.family_list.FamilyListFragment
import com.hr.kurtovic.tomislav.familyboard.fragments.ProfileFragment
import com.hr.kurtovic.tomislav.familyboard.main_board.MainBoardFragment
import kotlinx.android.synthetic.main.activity_board.*
import org.koin.android.ext.android.inject


class FragmentHolder : Fragment() {

    private val familyService: FamilyService by inject()
    private val familyMemberService: FamilyMemberService by inject()

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

        if (savedInstanceState == null) {
            bottom_navigation.selectedItemId = R.id.main_chat_item
            showFamilyBoard()
        }

        //TODO replace recreation of fragments every time the item changes
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.room_list_item -> {
                    showListRoom()
                }
                R.id.main_chat_item -> {
                    showFamilyBoard()
                }
                R.id.profile_item -> {
                    showProfile()
                }
            }
            false
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
        fragmentManager?.commit {
            replace(R.id.main_board_fragment_container, fragment, tag)
        }
    }
}
