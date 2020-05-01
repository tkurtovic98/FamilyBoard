package com.hr.kurtovic.tomislav.familyboard.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hr.kurtovic.tomislav.familyboard.MainActivity
import com.hr.kurtovic.tomislav.familyboard.R
import kotlinx.android.synthetic.main.profile_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModel()

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.profile_fragment, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel.familyChange.observe(viewLifecycleOwner, Observer { configureSpinner(it) })
        profile_logout_button.setOnClickListener { logout() }
    }

    private fun logout() {
        profileViewModel.logout()
        (activity as? MainActivity)?.showLoginScreen()
    }

    private fun configureSpinner(families: List<String>) {
        val familyAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.custom_spinner_dropdown,
            families
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_dropdown)
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
                    val familyName = adapter.getItem(position).toString()
                    Toast.makeText(requireContext(), "$familyName selected!", Toast.LENGTH_LONG)
                            .show()
                }

            })
        }

    }


}
