package com.pantaubersama.app.ui.note

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.toDp
import kotlinx.android.synthetic.main.carousel_party_item_layout.*
import timber.log.Timber

class CarouselPartyItemFragment : CommonFragment() {

    override fun setLayout(): Int {
        return R.layout.carousel_party_item_layout
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        val layoutParams = LinearLayout.LayoutParams(290f.toDp(requireContext()), 290f.toDp(requireContext()))

        party_images.layoutParams = layoutParams

        if (arguments?.getSerializable(PARTY) != null) {
            (arguments?.getSerializable(PARTY) as PoliticalParty).let {
                Timber.d(it.toString())
                party_images.loadUrl(it.image?.medium?.url)
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
        private val PARTY = "party"
        private val SCALE = "scale"

        fun newInstance(party: PoliticalParty, scale: Float): CarouselPartyItemFragment {
            val b = Bundle()
            b.putSerializable(PARTY, party)
            b.putFloat(SCALE, scale)
            val fragment = CarouselPartyItemFragment()
            fragment.arguments = b
            return fragment
        }
    }
}
