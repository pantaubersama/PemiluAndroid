package com.pantaubersama.app.ui.widget

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_CAMERA
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_STORAGE

class ImageChooserDialog(context: Context) {
    private var context: Context

    init {
        this.context = context
        val items = arrayOf<CharSequence>("Kamera", "Galeri",
            "Batal")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Ambil gambar dari")
        builder.setItems(items) { dialog, item ->
            when (item) {
                0 -> openCamera()
                1 -> openGallery()
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    fun openCamera(): Uri? {
        val values = ContentValues()
        val imageUriFromCamera = context.contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera)
        (context as FragmentActivity).startActivityForResult(intent, RC_CAMERA)
    }

    private fun openGallery() {
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        (context as FragmentActivity).startActivityForResult(Intent.createChooser(intentGallery, "Pilih"), RC_STORAGE)
    }
}