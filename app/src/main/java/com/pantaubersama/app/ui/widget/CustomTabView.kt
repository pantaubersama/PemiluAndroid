package com.pantaubersama.app.ui.widget

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.pantaubersama.app.R

/**
 * @author edityomurti on 18/12/2018 15:28
 */
class CustomTabView(context: Context) : LinearLayout(context) {
    private lateinit var tvTabTitle: TextView

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.layout_custom_tab, this)
        initView(view)
    }

    private fun initView(view: View) {
        tvTabTitle = view.findViewById(R.id.tv_tab_title)
    }

    fun setTitleLabel(title: CharSequence?) {
        tvTabTitle.text = title
    }

    fun setTitleLabel(@StringRes title: Int) {
        tvTabTitle.setText(title)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)

        tvTabTitle.setTextColor(if (selected) ContextCompat.getColor(context, R.color.white) else ContextCompat.getColor(context, R.color.gray_7))
        tvTabTitle.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
    }
}