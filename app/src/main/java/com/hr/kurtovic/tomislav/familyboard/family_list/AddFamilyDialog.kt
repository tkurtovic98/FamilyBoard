package com.hr.kurtovic.tomislav.familyboard.family_list

import android.content.Context
import android.view.LayoutInflater
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hr.kurtovic.tomislav.familyboard.R
import kotlinx.android.synthetic.main.add_family_dialog.view.*

interface AddFamilyDialogClickListener {
    fun submitFamilyName()
    fun afterTextChange(familyName: String)
}

class AddFamilyDialog(private val familyDialogClickListener: AddFamilyDialogClickListener) {

    fun constructAndReturnDialogBuilder(context: Context): MaterialAlertDialogBuilder {
        val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(context)

        val view = LayoutInflater.from(context).inflate(R.layout.add_family_dialog, null)

        builder.apply {
            setView(view)
        }

        view.add_family_input_name.doAfterTextChanged { familyDialogClickListener.afterTextChange(it.toString()) }
        builder.setPositiveButton(
            "Create"
        ) { _, _ ->
            familyDialogClickListener.submitFamilyName()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        return builder
    }

}

