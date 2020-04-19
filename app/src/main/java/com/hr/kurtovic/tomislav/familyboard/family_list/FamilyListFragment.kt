package com.hr.kurtovic.tomislav.familyboard.family_list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.hr.kurtovic.tomislav.familyboard.R
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyService
import com.hr.kurtovic.tomislav.familyboard.family_list.adapter.FamilyListAdapter
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
        familyListViewModel.internalState.observe(viewLifecycleOwner, Observer { render(it) })
        family_list_add_family.setOnClickListener { openAddFamilyDialog() }
    }

    private fun openAddFamilyDialog() {
        familyListViewModel.onEvent(Event.FamilyAdd)
    }

    private fun render(state: State) {
        family_list_no_families_text.isVisible = state.isEmpty

        if (state.submitInProgress) {
            Toast.makeText(context, "Submitting", Toast.LENGTH_LONG).show()
        }

        if (state.submitError) {
            Toast.makeText(context, "Error while submitting family", Toast.LENGTH_LONG).show()
        }

    }

    private fun configureRecyclerView() {
        //Configure Adapter & RecyclerView
        val familyListAdapter = FamilyListAdapter(
            generateOptionsForAdapter(familyService.showFamilies())
        ).apply {
            registerAdapterDataObserver(
                object : RecyclerView.AdapterDataObserver() {
                    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                        super.onItemRangeChanged(positionStart, itemCount)
                        familyListViewModel.onEvent(Event.ListDataChanged(itemCount == 0))
                    }
                }
            )
        }

        family_list_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = familyListAdapter
        }
    }

    private inline fun <reified T> generateOptionsForAdapter(query: Query): FirestoreRecyclerOptions<T> = FirestoreRecyclerOptions.Builder<T>()
            .setQuery(query, T::class.java)
            .setLifecycleOwner(this)
            .build()

}
