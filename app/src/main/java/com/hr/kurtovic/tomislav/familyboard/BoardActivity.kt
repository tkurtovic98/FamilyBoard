package com.hr.kurtovic.tomislav.familyboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.hr.kurtovic.tomislav.familyboard.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_board.*

class BoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

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