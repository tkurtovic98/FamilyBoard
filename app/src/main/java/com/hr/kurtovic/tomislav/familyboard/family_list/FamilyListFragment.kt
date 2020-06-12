package com.hr.kurtovic.tomislav.familyboard.family_list


import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyService
import com.hr.kurtovic.tomislav.familyboard.family_list.adapter.FamilyListAdapter
import com.hr.kurtovic.tomislav.familyboard.family_list.adapter.FamilyListItemListener
import kotlinx.android.synthetic.main.fragment_family_list.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
class FamilyListFragment : Fragment() {

    private val familyService: FamilyService by inject()
    private val familyMemberService: FamilyMemberService by inject()
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
        this.configureRecyclerView()
        familyListViewModel.state.observe(viewLifecycleOwner, Observer { render(it) })
        family_list_add_family.setOnClickListener { openAddFamilyDialog() }
    }

    private fun openAddFamilyDialog() {
        inputDialog()
    }

    private fun inputDialog() {
        //TODO(Change dialog layout)
        val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())

        builder.setTitle("Title")

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        input.doAfterTextChanged { familyListViewModel.onEvent(Event.FamilyNameChange(it.toString())) }
        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
            familyListViewModel.onEvent(Event.FamilyAdd)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun render(state: State) {
        if (state.loading) {
            family_list_layout.isVisible = false
            family_list_progress_bar.isVisible = true
            return
        } else {
            family_list_progress_bar.isVisible = false
            family_list_layout.isVisible = true
        }

        family_list_no_families_text.visibility = if (state.isEmpty) View.VISIBLE else View.GONE

        if (state.familyAddInProgress) {
            Snackbar.make(requireView(), R.string.family_add_message, Snackbar.LENGTH_LONG).show()
        }

        if (state.memberToFamilyAddInProgress) {
            Snackbar.make(requireView(), R.string.family_member_add_message, Snackbar.LENGTH_LONG)
                    .show()
        }

        state.submitError.take { Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show() }
    }

    private fun configureRecyclerView() {
        val familyListAdapter = FamilyListAdapter(
            generateOptionsForAdapter(familyService.showFamilies()),
            familyListItemListener()
        ).apply {
            registerAdapterDataObserver(
                object : RecyclerView.AdapterDataObserver() {
                    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                        super.onItemRangeRemoved(positionStart, itemCount)
                        familyListViewModel.onEvent(Event.ListDataChanged(itemCount == 0))
                    }

                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        super.onItemRangeInserted(positionStart, itemCount)
                        familyListViewModel.onEvent(Event.ListDataChanged(itemCount == 0))
                    }
                }
            )
        }

        family_list_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = familyListAdapter
        }

        familyListViewModel.onEvent(Event.RecyclerViewConfigured)
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
