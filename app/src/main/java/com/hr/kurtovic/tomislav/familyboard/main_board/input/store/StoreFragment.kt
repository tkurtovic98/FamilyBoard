package com.hr.kurtovic.tomislav.familyboard.main_board.input.store


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hr.kurtovic.tomislav.familyboard.R
import kotlinx.android.synthetic.main.fragment_store.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class StoreFragment : Fragment(), ItemCountChangeListener {

    private val storeViewModel: StoreViewModel by viewModel()

    companion object {
        fun newInstance() = StoreFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storeViewModel.state.observe(viewLifecycleOwner, Observer { render(it!!) })
        submit_button.setOnClickListener { submit() }
    }

    private fun submit() {
        storeViewModel.onEvent(Event.Submit)
    }

    private fun render(state: State) {
        store_input_progress_bar.isVisible = !state.recyclerViewConfigured

        if (state.storeItems.isNotEmpty() && !state.recyclerViewConfigured) {
            configureRecyclerView(state.storeItems)
            storeViewModel.onEvent(Event.RecyclerViewConfigured)
        }
    }

    private fun configureRecyclerView(storeItems: List<String>) {
        val itemsAdapter = StoreItemsAdapter(
            storeItems,
            this
        )
        store_items_recycler_view.apply {
            adapter = itemsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun countChange(item: String, count: Int) {
        storeViewModel.onEvent(Event.StoreItemCountChange(item, count))
    }


}
