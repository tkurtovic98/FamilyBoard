package com.hr.kurtovic.tomislav.familyboard.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.ui.ListRoomFragment
import com.hr.kurtovic.tomislav.familyboard.ui.ProfileFragment
import com.hr.kurtovic.tomislav.familyboard.ui.RoomChatFragment
import java.util.*

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragments: MutableList<Fragment> = arrayListOf()

    init {
        populateList()
    }


    private fun populateList() {
        fragments = ArrayList()
        fragments.add(ListRoomFragment())
        fragments.add(RoomChatFragment())
        fragments.add(ProfileFragment())
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return fragments.size
    }

    companion object {

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.list_tab, R.string.chat_tab, R.string.profile_tab)
    }
}