package com.hr.kurtovic.tomislav.familyboard.main_board.input.pets


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.hr.kurtovic.tomislav.familyboard.R
import kotlinx.android.synthetic.main.fragment_pets.*
import kotlinx.android.synthetic.main.fragment_pets.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class PetsFragment : Fragment() {

    private val petsViewModel: PetsViewModel by viewModel()

    companion object {
        fun newInstance() = PetsFragment()
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
        view.submit_button.setOnClickListener { submit() }
    }

    private fun submit() {
        petsViewModel.onEvent(Event.Submit)

        pets_what_input.text.clear()
        pets_who_input.text.clear()
        pets_when_input.text.clear()
    }
}
