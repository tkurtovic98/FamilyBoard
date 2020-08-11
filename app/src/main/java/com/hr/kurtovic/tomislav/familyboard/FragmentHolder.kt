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

/**
 *  This class is a parent fragment for
 *  MainBoardFragment,
 *  ProfileFragment,
 *  FamilyListFragment.
 * */

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
        fragmentHolderViewModel.state.observe(viewLifecycleOwner, Observer { render(it!!) })
        configureBottomNavigation()
    }

    private fun configureBottomNavigation() {
        val bottomNavigationItemMap = configureBottomNavigationItemMap()

        bottom_navigation.setOnNavigationItemSelectedListener { item ->

            if (bottomNavigationItemMap.containsKey(item.itemId)) {

                bottomNavigationItemMap[item.itemId].apply {
                    this?.invoke()
                }

                return@setOnNavigationItemSelectedListener true
            }

            false
        }
    }

    private fun configureBottomNavigationItemMap() =
            mapOf(
                R.id.bottom_nav_family_list_item to { showFamilyList() },
                R.id.bottom_nav_main_board_item to { showMainBoard() },
                R.id.bottom_nav_profile_item to { showProfile() }
            )

    private fun render(state: State) {
        fragment_holder_progress.isVisible = state.loading

        if (!state.loading) {
            bottom_navigation.selectedItemId = resolveSelectedItemId(state)
        }
    }

    private fun resolveSelectedItemId(state: State): Int {
        if (!state.hasSavedFamily) {
            return R.id.bottom_nav_family_list_item
        }

        if (state.savedFamilyName.isEmpty()) {
            return R.id.bottom_nav_profile_item
        }

        return R.id.bottom_nav_main_board_item
    }


    private fun showFamilyList() {
        replaceFragment(FamilyListFragment.newInstance())
    }

    private fun showMainBoard() {
        replaceFragment(MainBoardFragment.newInstance())
    }

    private fun showProfile() {
        replaceFragment(ProfileFragment.newInstance())
    }


    private fun replaceFragment(fragment: Fragment) {
        val tag = fragment::class.java.simpleName
        val currentFragment = parentFragmentManager.findFragmentByTag(tag)
        if (currentFragment?.isVisible == true) {
            return
        }
        parentFragmentManager.commit {
            this.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            replace(R.id.main_board_fragment_container, fragment, tag)
        }
    }
}
