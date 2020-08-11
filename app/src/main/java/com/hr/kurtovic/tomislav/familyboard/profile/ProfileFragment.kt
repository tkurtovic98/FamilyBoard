package com.hr.kurtovic.tomislav.familyboard.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.hr.kurtovic.tomislav.familyboard.MainActivity
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.models.FamilyMember
import com.hr.kurtovic.tomislav.familyboard.util.Box
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.view.*
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
        profileViewModel.state.observe(
            viewLifecycleOwner,
            Observer { render(it) }
        )
        profile_logout_button.setOnClickListener { logout() }
    }

    private fun logout() {
        profileViewModel.logout()
        (activity as? MainActivity)?.showLoginScreen()
    }

    private fun render(state: State) {
        profile_progress_bar.isVisible = !state.initialized || !state.spinnerConfigured

        if (!state.spinnerConfigured && state.initialized) {
            configureSpinner(state.families.map { it.name!! }, state.currentFamilyName!!)
            profileViewModel.onEvent(Event.SpinnerConfigured)
        }

        state.currentMember?.apply {
            renderMemberInfo(this)
        }
    }

    private fun renderMemberInfo(currentMember: FamilyMember) {

        if (profile_member_name.text.isNotEmpty()) {
            return
        }

        Glide.with(this)
                .load(currentMember.urlPicture)
                .circleCrop()
                .into(requireView().profile_profile_image)

        profile_member_name.text = currentMember.name
    }


    private fun configureSpinner(families: List<String>, savedFamilyName: String) {

        val savedFamilyNameBox = Box(savedFamilyName)

        val familyAdapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner,
            families
        ).apply {
            setDropDownViewResource(R.layout.custom_spinner_dropdown)
        }

        profile_family_spinner.apply {
            adapter = familyAdapter
            onItemSelectedListener = configureOnItemSelectedListener(
                familyAdapter,
                savedFamilyNameBox
            )
        }

    }

    private fun configureOnItemSelectedListener(
        adapter: ArrayAdapter<String>,
        savedFamilyNameBox: Box<String>
    ): OnItemSelectedListener {

        return object : OnItemSelectedListener {
            var previous = ""
            var current = ""

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                changeCurrentFamilyName(
                    adapter.getItem(position).toString()
                )
            }

            private fun changeCurrentFamilyName(
                newSelectedFamily: String
            ) {

                if (savedFamilyNameBox.content != null) {
                    firstTimeFamilyChange(savedFamilyNameBox, newSelectedFamily)
                    return
                }

                previous = current
                current = newSelectedFamily

                val notSameFamily = previous != current
                if (notSameFamily) {
                    profileViewModel.onEvent(Event.FamilyNameChange(newSelectedFamily))
                }

            }

            private fun firstTimeFamilyChange(
                savedFamilyNameBox: Box<String>,
                onSelectFamily: String
            ) {

                val savedFamilyName = savedFamilyNameBox.content!!

                if (savedFamilyName.isEmpty()) {
                    profileViewModel.onEvent(Event.FamilyNameChange(onSelectFamily))
                    savedFamilyNameBox.content = null
                    return
                }

                savedFamilyNameBox.content = null

                profile_family_spinner.setSelection(adapter.getPosition(savedFamilyName))
            }

        }


    }


}
