package com.pantaubersama.app.base.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 * @author edityomurti on 16/12/2018 01:42
 */
abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

    abstract fun bind(item: T)
}