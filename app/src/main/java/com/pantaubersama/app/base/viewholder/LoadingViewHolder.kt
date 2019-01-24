package com.pantaubersama.app.base.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieDrawable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_loading.*

class LoadingViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind() {
        lottie_loading.playAnimation()
        lottie_loading.repeatCount = LottieDrawable.INFINITE
    }
}