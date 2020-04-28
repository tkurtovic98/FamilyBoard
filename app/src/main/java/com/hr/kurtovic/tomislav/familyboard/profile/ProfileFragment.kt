package com.hr.kurtovic.tomislav.familyboard.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hr.kurtovic.tomislav.familyboard.MainActivity
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.SharedViewModel
import com.hr.kurtovic.tomislav.familyboard.util.Box
import kotlinx.android.synthetic.main.profile_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModel()
    private val sharedViewModel: SharedViewModel by sharedViewModel()


    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.profile_fragment, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val currentFamilyName = Box(sharedViewModel.sharedFamilyName.value)
        profileViewModel.familyChange.observe(
            viewLifecycleOwner,
            Observer { configureSpinner(it, currentFamilyName) })
        profile_logout_button.setOnClickListener { logout() }
    }

    private fun logout() {
        profileViewModel.logout()
        (activity as? MainActivity)?.showLoginScreen()
    }

    private fun configureSpinner(families: List<String>, currentFamilyName: Box<String>) {
        val familyAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            families
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        profile_family_spinner.apply {
            adapter = familyAdapter

            onItemSelectedListener = (object : OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    changeCurrentFamilyName(currentFamilyName, adapter.getItem(position).toString())
                }

                private fun changeCurrentFamilyName(
                    currentFamilyName: Box<String>,
                    familyFromAdapter: String
                ) {
                    if (currentFamilyName.content != null) {
                        currentFamilyName.take {
                            setSelection(familyAdapter.getPosition(it))
                        }
                    } else {
                        sharedViewModel.changeFamilyName(familyFromAdapter)
                    }

                }

            })
        }

    }


}
