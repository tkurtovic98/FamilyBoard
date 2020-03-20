package com.hr.kurtovic.tomislav.familyboard.main_board.input


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hr.kurtovic.tomislav.familyboard.R

/**
 * A simple [Fragment] subclass.
 */
class PetsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pets, container, false)
    }


}
