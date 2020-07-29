package com.hr.kurtovic.tomislav.familyboard.main_board.input.event


import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hr.kurtovic.tomislav.familyboard.R
import kotlinx.android.synthetic.main.fragment_event.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class EventFragment : Fragment() {

    private val eventViewModel: EventViewModel by viewModel()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        event_what_input.doAfterTextChanged { eventViewModel.onEvent(Event.WhatInputChange(it.toString())) }
        event_when_input.doAfterTextChanged { eventViewModel.onEvent(Event.WhenInputChange(it.toString())) }

        event_when_input.apply {
            onFocusChangeListener = View.OnFocusChangeListener { v: View, b: Boolean ->
                if (b) {
                    makeDatePicker(v as EditText)
                }
            }
            setOnClickListener { makeDatePicker(this as EditText) }
        }

        eventViewModel.state.observe(viewLifecycleOwner, Observer { render(it!!) })

        submit_button.setOnClickListener { submit() }
    }

    private fun makeDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val picker = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                editText.setText("$year/$month/$day")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        picker.show()
    }

    private fun render(state: State) {
        event_input_progress_bar.isVisible = state.postingInProgress

    }

    private fun submit() {
        eventViewModel.onEvent(Event.Submit)

        event_what_input.text.clear()
        event_when_input.text.clear()
    }

}
