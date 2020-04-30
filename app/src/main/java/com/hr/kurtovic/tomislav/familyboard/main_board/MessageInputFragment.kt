package com.hr.kurtovic.tomislav.familyboard.main_board


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.main_board.input.EventFragment
import com.hr.kurtovic.tomislav.familyboard.main_board.input.StoreFragment
import com.hr.kurtovic.tomislav.familyboard.main_board.input.pets.PetsFragment
import kotlinx.android.synthetic.main.fragment_message_input.view.*

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
    ): View = inflater.inflate(R.layout.fragment_message_input, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            showPets()
        }

        view.message_input_radio_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.pets_radio_button -> showPets()
                R.id.store_radio_button -> showStore()
                R.id.event_radio_button -> showEvent()
            }
        }
    }


    private fun showPets() {
        replaceFragment(PetsFragment.newInstance())
    }

    private fun showStore() {
        replaceFragment(StoreFragment.newInstance())
    }

    private fun showEvent() {
        replaceFragment(EventFragment.newInstance())
    }


    private fun replaceFragment(fragment: Fragment) {
        val tag = fragment::class.java.simpleName
        fragment.arguments = arguments
        requireActivity().supportFragmentManager.commit {
            replace(R.id.message_input_container, fragment, tag)
        }
    }


}
