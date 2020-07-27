package com.hr.kurtovic.tomislav.familyboard.main_board.message_display

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hr.kurtovic.tomislav.familyboard.R
import kotlinx.android.synthetic.main.message_display.*
import kotlinx.android.synthetic.main.message_display.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MessageDisplayDialogFragment : DialogFragment() {

    companion object {
        private const val MESSAGE_ID = "message_id"
        fun newInstance(messageId: String) = MessageDisplayDialogFragment().apply {
            arguments = Bundle().apply {
                putString(MESSAGE_ID, messageId)
            }
        }
    }

    private val messageDisplayViewModel: MessageDisplayViewModel by viewModel {
        parametersOf(requireArguments().getString(MESSAGE_ID))
    }


    private fun render(state: State) {
        val message = state.message
        val author = state.memberWhoAccepted

        if (message!!.accepted) {
            Glide.with(requireContext())
                    .load(author?.urlPicture)
                    .apply(RequestOptions.circleCropTransform())
                    .into(requireView().message_display_profile_image)
        }

        message_display_title.text = author?.name
        var contents = ""
        for (content in message.content!!.values) {
            contents += content.plus("\n")
        }
        message_display_data.text = contents
        message_display_progress_bar.isVisible = false
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.message_display, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        messageDisplayViewModel.data.observe(viewLifecycleOwner, Observer { render(it) })
    }
}
