package com.pantaubersama.app.ui.note

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pantaubersama.app.R

abstract class CarouselPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val pager: ViewPager,
    private val itemCount: Int
) : FragmentPagerAdapter(fragmentManager), ViewPager.OnPageChangeListener {
    private var scale: Float = 0.toFloat()

    override fun getItem(position: Int): Fragment {
        var newPosition = position
        try {
            scale = if (newPosition == itemCount)
                BIG_SCALE
            else
                SMALL_SCALE

            newPosition %= itemCount
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return getItemFragment(newPosition, scale)
    }

    abstract fun getItemFragment(position: Int, scale: Float): Fragment

    override fun getCount(): Int {
        return itemCount
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        try {
            if (positionOffset in 0f..1f) {
                val cur = getRootView(position)
                val next = getRootView(position + 1)
                cur?.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset)
                next?.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPageSelected(position: Int) {
        setPosition(position)
    }

    abstract fun setPosition(position: Int)

    override fun onPageScrollStateChanged(state: Int) {}

    private fun getRootView(position: Int): CarouselLinearLayout? {
        return fragmentManager.findFragmentByTag(this.getFragmentTag(position))
            ?.view
            ?.findViewById(R.id.root_container)
    }

    private fun getFragmentTag(position: Int): String {
        return "android:switcher:" + pager.id + ":" + position
    }

    companion object {
        val BIG_SCALE = 1.0f
        val SMALL_SCALE = 0.7f
        val DIFF_SCALE = BIG_SCALE - SMALL_SCALE
    }
}