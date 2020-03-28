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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.MainActivity
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageService
import com.hr.kurtovic.tomislav.familyboard.main_board.adapter.MainBoardMessageAdapter
import kotlinx.android.synthetic.main.fragment_main_board.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class MainBoardFragment : Fragment() {

    companion object {
        fun newInstance() = MainBoardFragment()
    }

    private val messageService: FamilyMessageService by inject()
    private val mainBoardViewModel: MainBoardViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?
        , savedInstanceState: Bundle?
    )
            : View? = inflater.inflate(R.layout.fragment_main_board, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_board_input_add.setOnClickListener { openInputFragment() }
        mainBoardViewModel.board.observe(viewLifecycleOwner, Observer { render(it) })
    }

    private fun render(board: Board) {
        main_board_fragment_empty_message_tv.isVisible = board.isEmpty

        if (board.familyNameIsChanging) {
            this.configureRecyclerView(board.currentFamilyName)
            mainBoardViewModel.onEvent(Event.NewFamilyBoardLoaded)
        }
    }

    private fun openInputFragment() {
        (activity as? MainActivity)?.showMessageInputScreen()
    }


    private fun configureRecyclerView(name: String) {
        //Configure Adapter & RecyclerView
        val mainBoardMessageAdapter = MainBoardMessageAdapter(
            generateOptionsForAdapter(messageService.messages(familyName = name)),
            FirebaseAuth.getInstance().currentUser!!.uid
        )
        mainBoardMessageAdapter.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    main_board_recyclerview.smoothScrollToPosition(mainBoardMessageAdapter.itemCount)
                    mainBoardViewModel.onEvent(Event.BoardDataChange(itemCount == 0))
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    super.onItemRangeRemoved(positionStart, itemCount)
                    mainBoardViewModel.onEvent(Event.BoardDataChange(itemCount == 0))
                }

            }
        )

        main_board_recyclerview.layoutManager = LinearLayoutManager(context)
        main_board_recyclerview.adapter = mainBoardMessageAdapter
    }

    private inline fun <reified T> generateOptionsForAdapter(query: Query): FirestoreRecyclerOptions<T> = FirestoreRecyclerOptions.Builder<T>()
            .setQuery(query, T::class.java)
            .setLifecycleOwner(this)
            .build()
}
