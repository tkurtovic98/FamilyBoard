package com.hr.kurtovic.tomislav.familyboard.family_list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hr.kurtovic.tomislav.familyboard.R


/**
 * A simple [Fragment] subclass.
 */
class FamilyListFragment : Fragment() {

    companion object {
        fun newInstance() = FamilyListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_family_list, container, false)


}
