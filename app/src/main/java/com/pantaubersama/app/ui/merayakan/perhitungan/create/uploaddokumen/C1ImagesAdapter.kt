package com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.tps.image.Image
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.c1_images_layout.*
import java.io.File

class C1ImagesAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    inner class C1ImagesViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Image) {
            val file = File(Uri.parse(item.uri).path)
            c1_model_image.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
            c1_model_image_name.text = file.absolutePath.substring(file.absolutePath.lastIndexOf("/") + 1)
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