package com.pantaubersama.app.ui.profile.cluster.requestcluster

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.cluster.Category
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.profile.cluster.categery.ClusterCategoryActivity
import com.pantaubersama.app.ui.widget.ImageChooserTools
import com.pantaubersama.app.utils.ImageUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import kotlinx.android.synthetic.main.activity_request_cluster.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class RequestClusterActivity : BaseActivity<RequestClusterPresenter>(), RequestClusterView {

    val CHANGE_CATEGORY = 1
    private lateinit var imageToUpload: MultipartBody.Part
    private var imageFile: File? = null
    protected var categoryId: String = ""

    @Inject
    override lateinit var presenter: RequestClusterPresenter

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_request_cluster

    override fun fetchIntentExtra() {
        // not used yed
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Request Buat Cluster", R.color.white, 4f)
        onClickAction()
    }

    private fun onClickAction() {
        request_cluster_category.setOnClickListener {
            val intent = Intent(this@RequestClusterActivity, ClusterCategoryActivity::class.java)
            startActivityForResult(intent, CHANGE_CATEGORY)
        }
        add_image.setOnClickListener {
            ImageChooserTools.showDialog(this@RequestClusterActivity, object : ImageChooserTools.ImageChooserListener {
                override fun onClickCamera(file: File) {
                    imageFile = file
                }
            })
        }
        btn_send.setOnClickListener {
            if (edit_name.text?.length != 0) {
                if (categoryId != "") {
                    if (edit_description.text?.length != 0) {
                        presenter.requestCluster(edit_name.text.toString(), categoryId, edit_description.text.toString(), imageToUpload)
                    } else {
                        edit_description.error = getString(R.string.blank_alert)
                    }
                } else {
                    partai_selected.text = getString(R.string.blank_alert)
                    partai_selected.setTextColor(ContextCompat.getColor(this@RequestClusterActivity, R.color.colorPrimary))
                }
            } else {
                edit_name.error = getString(R.string.blank_alert)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHANGE_CATEGORY) {
                val category = data?.getSerializableExtra(PantauConstants.Cluster.CATEGORY) as Category
                partai_selected.text = category.name
                categoryId = category.id
            } else if (requestCode == PantauConstants.RequestCode.RC_CAMERA) {
                iv_user_avatar.setImageDrawable(getDrawable(R.drawable.ic_editor_image))
                ImageUtil.compressImage(this, imageFile!!, 2, object : ImageUtil.CompressorListener {
                    override fun onSuccess(file: File) {
                        imageFile = file
                        imageToUpload = createFromFile()
                        iv_user_avatar.setImageURI(Uri.parse(imageFile?.absolutePath))
                    }

                    override fun onFailed(throwable: Throwable) {
                        iv_user_avatar.setImageDrawable(getDrawable(R.drawable.ic_avatar_placeholder))
                    }
                })
            } else if (requestCode == PantauConstants.RequestCode.RC_STORAGE) {
                if (data != null) {
                    val file = ImageChooserTools.proccedImageFromStorage(data, this@RequestClusterActivity)
                    imageToUpload = createFromFile(file)
                    iv_user_avatar.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
                }
            }
        }
    }

    private fun createFromFile(file: File? = null): MultipartBody.Part {
        var avatar: MultipartBody.Part? = null
        if (file != null) {
            val type: String
            val extension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
            val reqFile = RequestBody.create(MediaType.parse(type), file)
            avatar = MultipartBody.Part.createFormData("image", file.name, reqFile)
        } else if (imageFile != null) {
            val type: String
            val extension = MimeTypeMap.getFileExtensionFromUrl(imageFile?.absolutePath)
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
            val reqFile = RequestBody.create(MediaType.parse(type), imageFile)
            avatar = MultipartBody.Part.createFormData("image", imageFile?.name, reqFile)
        }
        return avatar!!
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    override fun disableView() {
        request_cluster_container.enable(false)
        btn_send.enable(false)
    }

    override fun showSuccessRequestClusterAlert() {
        ToastUtil.show(this@RequestClusterActivity, getString(R.string.success_create_cluster_alert))
    }

    override fun onSuccessRequestAlert() {
        finish()
    }

    override fun enableView() {
        request_cluster_container.enable(true)
        btn_send.enable(true)
    }

    override fun showFailedRequestClusterAlert() {
        ToastUtil.show(this@RequestClusterActivity, getString(R.string.failed_request_cluster_alert))
    }
}
