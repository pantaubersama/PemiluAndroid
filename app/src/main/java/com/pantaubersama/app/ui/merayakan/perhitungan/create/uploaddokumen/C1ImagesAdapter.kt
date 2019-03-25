package com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.tps.image.Image
import com.pantaubersama.app.data.model.tps.image.ImageLocalModel
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.c1_images_layout.*
import java.io.File

class C1ImagesAdapter(private val isPublished: Boolean) : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun getItemViewType(position: Int): Int {
        if (isPublished) {
            return VIEW_TYPE_ONLINE
        } else {
            return VIEW_TYPE_OFFLINE
        }
    }

    inner class C1ImagesOfflineViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: ImageLocalModel) {
            val file = File(Uri.parse(item.uri).path)
            c1_model_image.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
            c1_model_image_name.text = file.absolutePath.substring(file.absolutePath.lastIndexOf("/") + 1)
            delete_action.setOnClickListener {
                listener?.onClickDelete(item, adapterPosition)
            }
        }
    }

    inner class C1ImagesOnlineViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Image) {
            c1_model_image.loadUrl(item.file.thumbnail.url, R.drawable.ic_avatar_placeholder)
            c1_model_image_name.text = item.file.thumbnail.url?.lastIndexOf("/")?.plus(1)?.let { item.file.thumbnail.url?.substring(it) }
            delete_action.setOnClickListener {
                ToastUtil.show(itemView.context, "Perhitungan kamu telah dikirim dan tidak dapat diubah")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONLINE) {
            return C1ImagesOnlineViewHolder(parent.inflate(R.layout.c1_images_layout))
        } else {
            return C1ImagesOfflineViewHolder(parent.inflate(R.layout.c1_images_layout))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is C1ImagesOnlineViewHolder) {
            (holder).bind(data[position] as Image)
        } else {
            (holder as C1ImagesOfflineViewHolder).bind(data[position] as ImageLocalModel)
        }
    }

    interface Listener {
        fun onClickDelete(item: ImageLocalModel, adapterPosition: Int)
    }

    companion object {
        const val VIEW_TYPE_OFFLINE = 0
        const val VIEW_TYPE_ONLINE = 1
    }
}