package com.pantaubersama.app.ui.widget.imagepreview

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.Gravity
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pantaubersama.app.R
import com.pantaubersama.app.background.downloadc1.DownloadC1Service
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_IMAGE_URL
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.fragment_image_preview.*

class ImagePreviewDialogFragment : DialogFragment() {

    private lateinit var imageUrl: String
    private var showDownload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getString(EXTRA_IMAGE_URL)?.let { url -> imageUrl = url }
            showDownload = it.getBoolean("show_download", showDownload)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_image.loadUrl(imageUrl, R.color.gray_3)
        if (showDownload) {
            download_image_button.visibility = View.VISIBLE
            download_image_button.setOnClickListener {
                if (isHaveStorageAndCameraPermission()) {
                    startDownload()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(PantauConstants.Permission.GET_IMAGE_PERMISSION, PantauConstants.RequestCode.RC_ASK_PERMISSIONS)
                    }
                }
            }
        } else {
            download_image_button.visibility = View.GONE
        }
    }

    private fun startDownload() {
        val intent = Intent(requireContext(), DownloadC1Service::class.java)
        intent.putExtra("image_url", imageUrl)
        requireActivity().startService(intent)
    }

    private fun isHaveStorageAndCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val storagePermission = requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val cameraPermission = requireActivity().checkSelfPermission(Manifest.permission.CAMERA)
            val writeExternalPermission = requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            !(storagePermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED || writeExternalPermission != PackageManager.PERMISSION_GRANTED)
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PantauConstants.RequestCode.RC_ASK_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startDownload()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return dialog
    }

    override fun onResume() {
        val window = dialog?.window
        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        window?.setLayout((size.x * 0.95).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        super.onResume()
    }

    companion object {
        private val TAG = ImagePreviewDialogFragment::class.java.simpleName

        @JvmStatic
        fun show(imageUrl: String, fragmentManager: FragmentManager, showDownload: Boolean = false) {
            val dialog = ImagePreviewDialogFragment()
            dialog.arguments = Bundle().apply {
                putString(EXTRA_IMAGE_URL, imageUrl)
                putBoolean("show_download", showDownload)
            }
            dialog.show(fragmentManager, TAG)
        }
    }
}
