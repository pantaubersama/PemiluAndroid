package com.pantaubersama.app.ui.widget.imagepreview

import android.app.Dialog
import android.graphics.Point
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
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_IMAGE_URL
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.fragment_image_preview.*

class ImagePreviewDialogFragment : DialogFragment() {

    private lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getString(EXTRA_IMAGE_URL)?.let { url -> imageUrl = url }
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
        fun show(imageUrl: String, fragmentManager: FragmentManager) {
            val dialog = ImagePreviewDialogFragment()
            dialog.arguments = Bundle().apply {
                putString(EXTRA_IMAGE_URL, imageUrl)
            }
            dialog.show(fragmentManager, TAG)
        }
    }
}
