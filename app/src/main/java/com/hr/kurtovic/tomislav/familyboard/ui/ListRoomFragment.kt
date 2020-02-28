package com.hr.kurtovic.tomislav.familyboard.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hr.kurtovic.tomislav.familyboard.R


/**
 * A simple [Fragment] subclass.
 */
class ListRoomFragment : Fragment() {

    companion object {
        fun newInstance() = ListRoomFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list_room, container, false)


}
