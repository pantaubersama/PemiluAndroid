package com.pantaubersama.app.ui.note

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.carousel_party_item_layout.*

class CarouselPartyItemFragment : CommonFragment() {

    override fun setLayout(): Int {
        return R.layout.carousel_party_item_layout
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        if (arguments?.getSerializable(PARTY) != null) {
            (arguments?.getSerializable(PARTY) as PoliticalParty).let {
                party_name.text = it.name
                party_images.loadUrl(it.image?.medium?.url)
                if (it.number != 0) {
                    party_number.text = "No. ${it.number}"
                }
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
