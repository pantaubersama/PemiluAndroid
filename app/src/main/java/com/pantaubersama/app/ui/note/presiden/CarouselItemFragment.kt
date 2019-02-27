package com.pantaubersama.app.ui.note.presiden

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.ui.note.CarouselLinearLayout
import com.pantaubersama.app.utils.extensions.toDp
import kotlinx.android.synthetic.main.carousel_item_layout.*

class CarouselItemFragment : CommonFragment() {

    override fun setLayout(): Int {
        return R.layout.carousel_item_layout
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        val layoutParams = LinearLayout.LayoutParams(290f.toDp(requireContext()), 290f.toDp(requireContext()))

        paslon_images.layoutParams = layoutParams
        arguments?.getInt(IMAGE)?.let {
            paslon_images.setImageResource(it)
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

        fun newInstance(image: Int, scale: Float): CarouselItemFragment {
            val b = Bundle()
            b.putInt(IMAGE, image)
            b.putFloat(SCALE, scale)
            val fragment = CarouselItemFragment()
            fragment.arguments = b
            return fragment
        }
    }
}
