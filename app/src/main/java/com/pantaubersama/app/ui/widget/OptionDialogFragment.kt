package com.pantaubersama.app.ui.widget

import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pantaubersama.app.utils.extensions.visibleIf

class OptionDialogFragment : BottomSheetDialogFragment() {

    private var optionLayout: Int = 0

    private var viewVisibilityArray = SparseBooleanArray()

    var listener: View.OnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        optionLayout = arguments?.getInt("optionLayout") ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(optionLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rootLayout = view as ViewGroup
        for (i in 0..(rootLayout.childCount - 1)) {
            val child = rootLayout.getChildAt(i)
            child.setOnClickListener(listener)
            child.visibleIf(viewVisibilityArray.get(child.id, true))
        }
    }

    fun setViewVisibility(viewId: Int, isVisible: Boolean) {
        viewVisibilityArray.put(viewId, isVisible)
    }

    companion object {
        fun newInstance(optionLayout: Int): OptionDialogFragment {
            return OptionDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt("optionLayout", optionLayout)
                }
            }
        }
    }
}