package com.pantaubersama.app.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class CustomTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    override fun setupWithViewPager(viewPager: ViewPager?) {
        super.setupWithViewPager(viewPager)

        if (viewPager != null) {
            (0 until tabCount).forEach {
                val tab = getTabAt(it)
                tab?.customView = createTabView(viewPager.adapter?.getPageTitle(it))
            }
        }
    }

    private fun createTabView(title: CharSequence?): CustomTabView {
        return CustomTabView(context).apply {
            setTitleLabel(title)
        }
    }
}