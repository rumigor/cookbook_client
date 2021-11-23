package ru.rumigor.cookbook.ui.recipeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.rumigor.cookbook.R

class Dialog : BottomSheetDialogFragment() {
    private var dialogListener: OnDialogListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.settings_dialogfragment, container,
            false
        )
        isCancelable = false
        view.findViewById<View>(R.id.btnDelete).setOnClickListener {
            dismiss()
            dialogListener?.onDialogDelete()
        }
        view.findViewById<View>(R.id.btnCancel).setOnClickListener {
            dismiss()
            dialogListener?.onDialogCancel()
        }
        return view
    }

    fun setOnDialogListener(dialogListener: OnDialogListener?) {
        this.dialogListener = dialogListener
    }

    companion object {
        fun newInstance(): Dialog {
            return Dialog()
        }
    }
}