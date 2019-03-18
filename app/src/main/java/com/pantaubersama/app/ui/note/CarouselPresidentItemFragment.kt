package com.pantaubersama.app.ui.note

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.utils.extensions.toDp
import kotlinx.android.synthetic.main.carousel_president_item_layout.*

class CarouselPresidentItemFragment : CommonFragment() {

    override fun setLayout(): Int {
        return R.layout.carousel_president_item_layout
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        val layoutParams = LinearLayout.LayoutParams(290f.toDp(requireContext()), 290f.toDp(requireContext()))

        party_images.layoutParams = layoutParams

        if (arguments?.getInt(IMAGE) != null) {
            arguments?.getInt(IMAGE)?.let {
                party_images.setImageResource(it)
            }
        }

        arguments?.getFloat(SCALE)?.let {
            (root_container as CarouselLinearLayout).setScaleBoth(it)
        }
    }

    /**
     * Get device screen width and height
     */
//    private fun getWidthAndHeight() {
//        val displaymetrics = DisplayMetrics()
//        activity?.windowManager?.defaultDisplay?.getMetrics(displaymetrics)
//        screenHeight = displaymetrics.heightPixels
//        screenWidth = displaymetrics.widthPixels
//    }

    companion object {
        private val IMAGE = "image"
        private val SCALE = "scale"

        fun newInstance(image: Int, scale: Float): CarouselPresidentItemFragment {
            val b = Bundle()
            b.putInt(IMAGE, image)
            b.putFloat(SCALE, scale)
            val fragment = CarouselPresidentItemFragment()
            fragment.arguments = b
            return fragment
        }
    }
}
