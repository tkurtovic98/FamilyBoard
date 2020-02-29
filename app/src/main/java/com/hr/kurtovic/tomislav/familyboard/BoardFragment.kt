package com.hr.kurtovic.tomislav.familyboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayout
import com.hr.kurtovic.tomislav.familyboard.ui.ListRoomFragment
import com.hr.kurtovic.tomislav.familyboard.ui.ProfileFragment
import com.hr.kurtovic.tomislav.familyboard.ui.RoomChatFragment
import kotlinx.android.synthetic.main.activity_board_2.*

class BoardFragment : Fragment() {

    companion object {
        fun newInstance() = BoardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?
        , savedInstanceState: Bundle?
    )
            : View? = inflater.inflate(R.layout.activity_board_2, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            showMainChat()
        }

        //TODO replace recreation of fragments every time the item changes
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.room_list_item -> {
                    showListRoom()
                }
                R.id.main_chat_item -> {
                    showMainChat()
                }
                R.id.profile_item -> {
                    showProfile()
                }
            }
            false
        }
    }

    private fun showListRoom() {
        replaceFragment(ListRoomFragment.newInstance())
    }

    private fun showMainChat() {
        replaceFragment(RoomChatFragment.newInstance())
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
//        val sectionsPagerAdapter = SectionsPagerAdapter(context!!, fragmentManager!!)
//
//        view_pager.adapter = sectionsPagerAdapter
//        tabs.setupWithViewPager(view_pager)
//
//        view_pager.currentItem = 1
//        setTabIcons(tabs!!)


private fun setTabIcons(tabs: TabLayout) {
    val icons = intArrayOf(
        R.drawable.ic_search_black_24dp,
        R.drawable.ic_home_black_24dp,
        R.drawable.ic_account_circle_black_24dp
    )

    for (i in 0 until tabs.tabCount) {
        tabs.getTabAt(i)!!.setIcon(icons[i])
    }
}
