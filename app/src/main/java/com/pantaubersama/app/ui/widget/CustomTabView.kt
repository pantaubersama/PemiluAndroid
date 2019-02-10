package com.pantaubersama.app.ui.widget

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.pantaubersama.app.R

/**
 * @author edityomurti on 18/12/2018 15:28
 */
class CustomTabView(context: Context) : LinearLayout(context) {

    private lateinit var tvTabTitle: TextView

    private val typefaceRegular = ResourcesCompat.getFont(context, R.font.lato_regular)
    private val typefaceBold = ResourcesCompat.getFont(context, R.font.lato_black)

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

        tvTabTitle.setTextColor(if (selected) ContextCompat.getColor(context, R.color.white) else ContextCompat.getColor(context, R.color.translucent_white_2))
        tvTabTitle.typeface = if (selected) typefaceBold else typefaceRegular
    }
}