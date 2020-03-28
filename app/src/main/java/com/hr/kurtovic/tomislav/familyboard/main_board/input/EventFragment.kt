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
class EventFragment : Fragment() {

    companion object {
        fun newInstance() = EventFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }


}
