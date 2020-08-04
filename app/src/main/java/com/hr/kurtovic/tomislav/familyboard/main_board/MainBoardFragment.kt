package com.hr.kurtovic.tomislav.familyboard.main_board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.MainActivity
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.main_board.adapter.MainBoardMessageAdapter
import com.hr.kurtovic.tomislav.familyboard.main_board.adapter.MenuItemClickListener
import com.hr.kurtovic.tomislav.familyboard.main_board.adapter.PopupMenuItem
import com.hr.kurtovic.tomislav.familyboard.models.Message
import kotlinx.android.synthetic.main.fragment_main_board.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class MainBoardFragment : Fragment(), MenuItemClickListener {

    private val mainBoardViewModel: MainBoardViewModel by viewModel()

    companion object {
        fun newInstance() = MainBoardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?
        , savedInstanceState: Bundle?
    )
            : View? = inflater.inflate(R.layout.fragment_main_board, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainBoardViewModel.state.observe(viewLifecycleOwner, Observer { render(it) })

        main_board_input_add.setOnClickListener { openInputFragment() }
    }

    private fun render(state: State) {
        main_board_progress_bar.isVisible = !state.recycleViewConfigured || state.actionInProgress
        main_board_input_add.isVisible = state.recycleViewConfigured

        main_board_fragment_empty_message_tv.isVisible = state.isEmpty && state.recycleViewConfigured

        if (!state.recycleViewConfigured && state.currentMember != null) {
            this.configureRecyclerView(state)
            this.mainBoardViewModel.onEvent(Event.RecyclerViewConfigured)
        }

        state.errorMessage.take { Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show() }
    }

    private fun openInputFragment() {
        (activity as? MainActivity)?.showMessageInputScreen()
    }


    private fun configureRecyclerView(state: State) {
        val mainBoardMessageAdapter = MainBoardMessageAdapter(
            generateOptionsForAdapter(state.messageQuery!!),
            state.currentMember?.uid!!,
            this
        ).apply {
            registerAdapterDataObserver(adapterDataObserver())
        }

        main_board_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainBoardMessageAdapter
        }
    }

    private fun adapterDataObserver() =
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    super.onItemRangeChanged(positionStart, itemCount)
                    if (itemCount == 0) {
                        mainBoardViewModel.onEvent(Event.BoardDataChange(isEmpty = true))
                    }
                }
            }

    private inline fun <reified T> generateOptionsForAdapter(query: Query): FirestoreRecyclerOptions<T> = FirestoreRecyclerOptions.Builder<T>()
            .setQuery(query, T::class.java)
            .setLifecycleOwner(this)
            .build()

    override fun onMenuItemClick(message: Message, menuItem: PopupMenuItem) {
        when (menuItem) {
            PopupMenuItem.ACCEPTED -> mainBoardViewModel.onEvent(Event.SetMessageAccepted(messageId = message.id!!))
            PopupMenuItem.SHOW -> showMessageDisplay(message.id!!)
            PopupMenuItem.DELETE -> mainBoardViewModel.onEvent(Event.DeleteMessage(messageId = message.id!!))
        }
    }

    private fun showMessageDisplay(messageId: String) {
        (activity as MainActivity).showMessageDisplay(messageId = messageId)
    }
}
