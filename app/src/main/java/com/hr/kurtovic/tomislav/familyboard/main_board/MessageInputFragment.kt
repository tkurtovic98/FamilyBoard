package com.hr.kurtovic.tomislav.familyboard.main_board


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hr.kurtovic.tomislav.familyboard.R

/**
 * A simple [Fragment] subclass.
 */
class MessageInputFragment : Fragment() {

    companion object {
        fun newInstance() = MessageInputFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        todo set checked listener to replace fragments
//        view.message_input_radio_group.setOnCheckedChangeListener {group, checkedId ->
//        }
    }

//    private fun showPets() {
//        replaceFragment(ListRoomFragment.newInstance())
//    }
//
//    private fun showStore() {
//        replaceFragment(MainBoardFragment.newInstance())
//    }
//
//    private fun showEvent() {
//        replaceFragment(ProfileFragment.newInstance())
//    }
//
//
//    private fun replaceFragment(fragment: Fragment) {
//        val tag = fragment::class.java.simpleName
//        fragmentManager?.commit {
//            replace(R.id.message_input_container, fragment, tag)
//        }
//    }


}
