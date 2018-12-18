package com.pantaubersama.app.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.pantaubersama.app.R

/**
 * @author edityomurti on 18/12/2018 15:28
 */
class TabView : LinearLayout {
    private lateinit var tvTabTitle: TextView

    constructor (context: Context?) : super(context) {
        init()
    }

    private fun init() {
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflater.inflate(R.layout.layout_custom_tab, this)
        initView(view)
    }

    private fun initView(view: View) {
        tvTabTitle = view.findViewById(R.id.tv_tab_title)
    }

    fun setTitleLabel(title: String) {
        tvTabTitle.text = title
    }

    fun setTitleLabel(@StringRes title: Int) {
        tvTabTitle.setText(title)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)

        tvTabTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
        tvTabTitle.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
    }
}