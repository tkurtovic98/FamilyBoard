package com.hr.kurtovic.tomislav.familyboard.family_list


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
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.family_list.adapter.FamilyListAdapter
import com.hr.kurtovic.tomislav.familyboard.family_list.adapter.FamilyListItemListener
import kotlinx.android.synthetic.main.fragment_family_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
class FamilyListFragment : Fragment(), AddFamilyDialogClickListener {

    private val familyListViewModel: FamilyListViewModel by viewModel()

    companion object {
        fun newInstance() = FamilyListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_family_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        familyListViewModel.state.observe(viewLifecycleOwner, Observer { render(it) })
        family_list_add_family.setOnClickListener { openAddFamilyDialog() }
    }

    override fun submitFamilyName() {
        familyListViewModel.onEvent(Event.FamilyAdd)
    }

    override fun afterTextChange(familyName: String) {
        familyListViewModel.onEvent(Event.FamilyNameChange(familyName))
    }

    private fun openAddFamilyDialog() {
        inputDialog()
    }

    private fun inputDialog() {
        AddFamilyDialog(this)
                .constructAndReturnDialogBuilder(requireContext())
                .show()
    }

    private fun render(state: State) {
        family_list_recycler_view_container.isVisible = state.recyclerViewConfigured
        family_list_progress_bar.isVisible = !state.recyclerViewConfigured

        family_list_no_families_text.isVisible = state.isEmpty && state.recyclerViewConfigured

        if (state.familyAddInProgress) {
            Snackbar.make(requireView(), R.string.family_add_message, Snackbar.LENGTH_LONG).show()
        }

        if (state.memberToFamilyAddInProgress) {
            Snackbar.make(requireView(), R.string.family_member_add_message, Snackbar.LENGTH_LONG)
                    .show()
        }

        state.submitError.take {
            Snackbar.make(requireView(), getString(it), Snackbar.LENGTH_LONG).show()
        }

        if (!state.recyclerViewConfigured) {
            configureRecyclerView(state)
            familyListViewModel.onEvent(Event.RecyclerViewConfigured)
        }
    }


    private fun configureRecyclerView(state: State) {
        val familyListAdapter = FamilyListAdapter(
            generateOptionsForAdapter(state.familyListQuery!!),
            familyListItemListener()
        ).apply {
            registerAdapterDataObserver(adapterDataObserver())
        }

        family_list_recycler_view.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = familyListAdapter
        }
    }

    private fun adapterDataObserver(): RecyclerView.AdapterDataObserver {
        return object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                if (itemCount == 0) {
                    familyListViewModel.onEvent(Event.ListDataChanged(true))
                }
            }
        }
    }

    private fun familyListItemListener(): FamilyListItemListener =
            object : FamilyListItemListener {
                override fun onItemClick(familyName: String) {
                    familyListViewModel.onEvent(Event.FamilyMemberAdd(familyName))
                }
            }

    private inline fun <reified T> generateOptionsForAdapter(query: Query): FirestoreRecyclerOptions<T> = FirestoreRecyclerOptions.Builder<T>()
            .setQuery(query, T::class.java)
            .setLifecycleOwner(this)
            .build()


}
