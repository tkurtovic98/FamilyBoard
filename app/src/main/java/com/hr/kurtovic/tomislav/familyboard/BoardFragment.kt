package com.hr.kurtovic.tomislav.familyboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.hr.kurtovic.tomislav.familyboard.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_board.*

class BoardFragment : Fragment() {

    companion object {
        fun newInstance() = BoardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?
        , savedInstanceState: Bundle?
    )
            : View? = inflater.inflate(R.layout.activity_board, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionsPagerAdapter = SectionsPagerAdapter(context!!, fragmentManager!!)

        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        view_pager.currentItem = 1
        setTabIcons(tabs!!)
    }


    private fun setTabIcons(tabs: TabLayout) {
        val icons = intArrayOf(R.drawable.ic_search_black_24dp, R.drawable.ic_home_black_24dp, R.drawable.ic_account_circle_black_24dp)

        for (i in 0 until tabs.tabCount) {
            tabs.getTabAt(i)!!.setIcon(icons[i])
        }
    }
}