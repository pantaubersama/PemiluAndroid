package com.pantaubersama.app.ui.note.presiden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.note.CarouselLinearLayout
import com.pantaubersama.app.utils.extensions.toDp
import kotlinx.android.synthetic.main.carousel_item_layout.view.*

class CarouselItemFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (container == null) {
            return null
        }

        val layoutParams = LinearLayout.LayoutParams(300f.toDp(requireContext()), 300f.toDp(requireContext()))
        val linearLayout = inflater.inflate(R.layout.carousel_item_layout, container, false) as LinearLayout

        linearLayout.paslon_images.layoutParams = layoutParams
        arguments?.getInt(IMAGE)?.let {
            linearLayout.paslon_images.setImageResource(it)
        }

        arguments?.getFloat(SCALE)?.let {
            (linearLayout.root_container as CarouselLinearLayout).setScaleBoth(it)
        }

        return linearLayout
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
