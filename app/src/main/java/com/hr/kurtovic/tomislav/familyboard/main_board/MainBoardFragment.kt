package com.hr.kurtovic.tomislav.familyboard.main_board


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.CurrentBoardKeyHolder
import com.hr.kurtovic.tomislav.familyboard.MainActivity
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.api.MessageHelper
import com.hr.kurtovic.tomislav.familyboard.api.UserHelper
import com.hr.kurtovic.tomislav.familyboard.main_board.utils.MainBoardMessageAdapter
import com.hr.kurtovic.tomislav.familyboard.models.Message
import com.hr.kurtovic.tomislav.familyboard.models.User
import kotlinx.android.synthetic.main.fragment_main_board.*

/**
 * A simple [Fragment] subclass.
 */
class MainBoardFragment : Fragment(), MainBoardMessageAdapter.Listener {


    companion object {
        fun newInstance() = MainBoardFragment()
    }

    // FOR DATA

    // 2 - Declaring Adapter and data
    private lateinit var mainBoardMessageAdapter: MainBoardMessageAdapter
    private lateinit var modelCurrentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?
        , savedInstanceState: Bundle?
    )
            : View? = inflater.inflate(R.layout.fragment_main_board, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        main_board_fragment_empty_message_tv.visibility = View.GONE
        this.getCurrentUserFromFirestore()
        this.configureRecyclerView()

        main_board_input_add.setOnClickListener { openInputFragment() }
    }

    override fun onDataChanged() {
        main_board_fragment_empty_message_tv.visibility = if (this.mainBoardMessageAdapter.itemCount == 0)
            View.VISIBLE
        else
            View.GONE
    }

    private fun openInputFragment() {
        (activity as? MainActivity)?.showMessageInputScreen()
    }


    private fun getCurrentUserFromFirestore() {
        UserHelper.getUser(FirebaseAuth.getInstance().currentUser!!.uid)
                .addOnSuccessListener { documentSnapshot ->
                    modelCurrentUser = documentSnapshot.toObject(
                        User::class.java
                    )!!
                }
    }

    private fun configureRecyclerView() {
        //Configure Adapter & RecyclerView
        this.mainBoardMessageAdapter = MainBoardMessageAdapter(
            generateOptionsForAdapter(MessageHelper.getAllMessageForChat(CurrentBoardKeyHolder.getInstance()!!.currentKey)),
            this,
            FirebaseAuth.getInstance().currentUser!!.uid
        )
        mainBoardMessageAdapter.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    main_board_recyclerview.smoothScrollToPosition(mainBoardMessageAdapter.itemCount)
                }
            }
        )

        main_board_recyclerview.layoutManager = LinearLayoutManager(context)
        main_board_recyclerview.adapter = this.mainBoardMessageAdapter
    }

    private fun generateOptionsForAdapter(query: Query): FirestoreRecyclerOptions<Message> = FirestoreRecyclerOptions.Builder<Message>()
            .setQuery(query, Message::class.java)
            .setLifecycleOwner(this)
            .build()
}
