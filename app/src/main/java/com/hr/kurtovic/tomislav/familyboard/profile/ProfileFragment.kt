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
import com.bumptech.glide.Glide
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
        profileViewModel.onEvent(Event.FamilyNameChange(familyName = sharedViewModel.sharedFamilyName.value!!))
        profileViewModel.state.observe(
            viewLifecycleOwner,
            Observer { render(it) })
        profile_logout_button.setOnClickListener { logout() }
    }

    private fun logout() {
        profileViewModel.logout()
        (activity as? MainActivity)?.showLoginScreen()
    }

    private fun render(state: State) {
        profile_progress_bar.visibility = if (state.loading) {
            View.VISIBLE
            return
        } else View.GONE

        if (state.spinnerConfigure) {
            configureSpinner(state.familyList, state.currentFamilyName)
            profileViewModel.onEvent(Event.SpinnerConfigured)
        }

        if (state.firstTimeLoading) {
            state.currentMember?.apply {
                Glide.with(requireContext())
                        .load(this.urlPicture)
                        .circleCrop()
                        .into(profile_profile_image)

                profile_member_name.text = this.name
            }
            profileViewModel.onEvent(Event.FirstTimeLoaded)
        }
    }


    private fun configureSpinner(families: List<String>, familyName: Box<String>) {
        val familyAdapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner,
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
                    changeCurrentFamilyName(adapter.getItem(position).toString())
                }

                private fun changeCurrentFamilyName(
                    familyFromAdapter: String
                ) {
                    if (familyName.content != null) {
                        familyName.take {
                            if (it != familyFromAdapter){
                                setSelection(familyAdapter.getPosition(it))
                            }
                        }
                    } else {
                        sharedViewModel.changeFamilyName(familyFromAdapter)
                    }

                }

            })
        }

    }


}
