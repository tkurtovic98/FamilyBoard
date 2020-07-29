package com.hr.kurtovic.tomislav.familyboard.main_board.message_display

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.hr.kurtovic.tomislav.familyboard.R
import kotlinx.android.synthetic.main.message_display.*
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
        val memberWhoAccepted = state.memberWhoAccepted

        message_display_title.text = if (message!!.accepted) {
            getString(
                R.string.message_accept_text,
                memberWhoAccepted?.name!!
            )
        } else {
            getString(R.string.message_display_not_accepted)
        }

        memberWhoAccepted?.let {
            message_display_title.text = it.name!!
        }

        val contentContainer = message_display_content_container

        for (content in message.content!!) {
            val title = createTextView(
                content = content.key,
                textAppearance = android.R.style.TextAppearance_Material_Display2
            )
            val messageText = createTextView(
                content = content.value,
                textAppearance = android.R.style.TextAppearance_Material_Display1
            )
            contentContainer.addView(title)
            contentContainer.addView(messageText)
        }
        message_display_progress_bar.isVisible = false
    }

    private fun createTextView(content: String, textAppearance: Int): TextView {
        val textView = TextView(requireContext())
        textView.requestLayout()
        textView.text = content
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        textView.setTextAppearance(textAppearance)
        textView.gravity = Gravity.CENTER
        return textView
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
