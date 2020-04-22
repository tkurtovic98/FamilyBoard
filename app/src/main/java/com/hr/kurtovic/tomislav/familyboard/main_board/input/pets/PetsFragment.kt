package com.hr.kurtovic.tomislav.familyboard.main_board.input.pets


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.main_board.MainBoardViewModel
import kotlinx.android.synthetic.main.fragment_pets.*
import kotlinx.android.synthetic.main.fragment_pets.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class PetsFragment : Fragment() {

    private val petsViewModel: PetsViewModel by viewModel()
    private val mainBoardViewModel: MainBoardViewModel by viewModel()

    companion object {
        fun newInstance() = PetsFragment()
        private var familyName = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pets, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pets_what_input.doAfterTextChanged { petsViewModel.onEvent(Event.WhatInputChange(it.toString())) }
        pets_who_input.doAfterTextChanged { petsViewModel.onEvent(Event.WhoInputChange(it.toString())) }
        pets_when_input.doAfterTextChanged { petsViewModel.onEvent(Event.UntilWhenInputChange(it.toString())) }
        view.submit_button.setOnClickListener { petsViewModel.onEvent(Event.Submit(familyName)) }

        petsViewModel.input.observe(viewLifecycleOwner, Observer { render(it) })
        mainBoardViewModel.board.observe(
            viewLifecycleOwner,
            Observer { familyName = it.currentFamilyName })
    }

    private fun render(input: Input) {
        if (input.postingInProgress) {
            pets_what_input.setText(input.whatInput)
            pets_who_input.setText(input.whoInput)
            pets_when_input.setText(input.untilWhenInput)
        }
    }

}
