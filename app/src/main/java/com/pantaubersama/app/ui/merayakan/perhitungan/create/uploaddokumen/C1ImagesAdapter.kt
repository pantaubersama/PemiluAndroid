package com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen

import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.tps.Image
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.c1_images_layout.*

class C1ImagesAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    inner class C1ImagesViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Image) {
            c1_model_image.setImageBitmap(BitmapFactory.decodeFile(item.file.absolutePath))
            c1_model_image_name.text = item.file.absolutePath.substring(item.file.absolutePath.lastIndexOf("/") + 1)
            delete_action.setOnClickListener {
                listener?.onClickDelete(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return C1ImagesViewHolder(parent.inflate(R.layout.c1_images_layout))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as C1ImagesViewHolder).bind(data[position] as Image)
    }

    interface Listener {
        fun onClickDelete(item: Image, adapterPosition: Int)
    }
}