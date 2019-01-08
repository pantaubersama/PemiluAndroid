package com.pantaubersama.app.ui.widget

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_CAMERA
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_STORAGE

class ImageChooserDialog(private val context: Context) {
    private var builder: AlertDialog.Builder
    init {
        val items = arrayOf<CharSequence>(context.getString(R.string.kamera), context.getString(R.string.galeri))
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.ambil_gambar_dari))
        builder.setItems(items) { dialog, item ->
            when (item) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        this.builder = builder
    }

    fun show() {
        builder.show()
    }

    fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        (context as FragmentActivity).startActivityForResult(intent, RC_CAMERA)
    }

    private fun openGallery() {
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        (context as FragmentActivity).startActivityForResult(Intent.createChooser(intentGallery, "Pilih"), RC_STORAGE)
    }
}