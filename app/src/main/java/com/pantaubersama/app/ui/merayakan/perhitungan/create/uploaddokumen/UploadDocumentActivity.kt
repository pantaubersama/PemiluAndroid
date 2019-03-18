package com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.tps.Image
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.widget.ImageChooserTools
import com.pantaubersama.app.utils.ImageUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_upload_document.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UploadDocumentActivity : BaseActivity<UploadDocumentPresenter>(), UploadDocumentView {

    @Inject
    override lateinit var presenter: UploadDocumentPresenter
    private lateinit var c1PresidenFiles: MutableList<File>
    private lateinit var c1DprFiles: MutableList<File>
    private lateinit var c1DpdFiles: MutableList<File>
    private lateinit var c1DprdProvFiles: MutableList<File>
    private lateinit var c1DprdKabFiles: MutableList<File>
    private lateinit var suasanaTpsFiles: MutableList<File>
//    private lateinit var c1PresidenImagesPart: MutableList<MultipartBody.Part>
    private lateinit var c1PresidenAdapter: C1ImagesAdapter
    private lateinit var c1DprAdapter: C1ImagesAdapter
    private lateinit var c1DpdAdapter: C1ImagesAdapter
    private lateinit var c1DprdProvAdapter: C1ImagesAdapter
    private lateinit var c1DprdKabAdapter: C1ImagesAdapter
    private lateinit var suasanaTpsAdapter: C1ImagesAdapter
    private var uploadType = ""

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_upload_document
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Unggah", R.color.white, 4f)
        save_button.setOnClickListener {
            finish()
        }
        setupC1Presiden()
        setupC1Dpr()
        setupC1Dpd()
        setupC1DprdProv()
        setupC1DprdKab()
        setupSuasanaTps()
    }

    private fun setupSuasanaTps() {
        c1_suasana_tps_list
        suasanaTpsFiles = ArrayList()
//        c1PresidenImagesPart = ArrayList()
        suasanaTpsAdapter = C1ImagesAdapter()
        suasanaTpsAdapter.listener = object : C1ImagesAdapter.Listener {
            override fun onClickDelete(item: Image, adapterPosition: Int) {
                suasanaTpsAdapter.deleteItem(adapterPosition)
            }
        }
        c1_suasana_tps_list.layoutManager = LinearLayoutManager(this@UploadDocumentActivity)
        c1_suasana_tps_list.adapter = suasanaTpsAdapter
        add_tps_images_button.setOnClickListener {
            if (suasanaTpsAdapter.getListData().size < 3) {
                uploadType = "suasana_tps"
                showImageChooserDialog()
            } else {
                ToastUtil.show(this@UploadDocumentActivity, "Gambar maksimal 3 item")
            }
        }
    }

    private fun setupC1DprdKab() {
        c1DprdKabFiles = ArrayList()
//        c1PresidenImagesPart = ArrayList()
        c1DprdKabAdapter = C1ImagesAdapter()
        c1DprdKabAdapter.listener = object : C1ImagesAdapter.Listener {
            override fun onClickDelete(item: Image, adapterPosition: Int) {
                c1DprdKabAdapter.deleteItem(adapterPosition)
            }
        }
        c1_dprd_kab_list.layoutManager = LinearLayoutManager(this@UploadDocumentActivity)
        c1_dprd_kab_list.adapter = c1DprdKabAdapter
        add_c1_dprd_kabupaten_button.setOnClickListener {
            if (c1DprdKabAdapter.getListData().size < 5) {
                uploadType = "dprd_kab"
                showImageChooserDialog()
            } else {
                ToastUtil.show(this@UploadDocumentActivity, "Gambar maksimal 5 item")
            }
        }
    }

    private fun setupC1DprdProv() {
        c1DprdProvFiles = ArrayList()
//        c1PresidenImagesPart = ArrayList()
        c1DprdProvAdapter = C1ImagesAdapter()
        c1DprdProvAdapter.listener = object : C1ImagesAdapter.Listener {
            override fun onClickDelete(item: Image, adapterPosition: Int) {
                c1DprdProvAdapter.deleteItem(adapterPosition)
            }
        }
        c1_dprd_prov_list.layoutManager = LinearLayoutManager(this@UploadDocumentActivity)
        c1_dprd_prov_list.adapter = c1DprdProvAdapter
        add_c1_dprd_provinsi_button.setOnClickListener {
            if (c1DprdProvAdapter.getListData().size < 5) {
                uploadType = "dprd_prov"
                showImageChooserDialog()
            } else {
                ToastUtil.show(this@UploadDocumentActivity, "Gambar maksimal 5 item")
            }
        }
    }

    private fun setupC1Dpd() {
        c1DpdFiles = ArrayList()
//        c1PresidenImagesPart = ArrayList()
        c1DpdAdapter = C1ImagesAdapter()
        c1DpdAdapter.listener = object : C1ImagesAdapter.Listener {
            override fun onClickDelete(item: Image, adapterPosition: Int) {
                c1DpdAdapter.deleteItem(adapterPosition)
            }
        }
        c1_dpd_list.layoutManager = LinearLayoutManager(this@UploadDocumentActivity)
        c1_dpd_list.adapter = c1DpdAdapter
        add_c1_dpd_button.setOnClickListener {
            if (c1DpdAdapter.getListData().size < 5) {
                uploadType = "dpd"
                showImageChooserDialog()
            } else {
                ToastUtil.show(this@UploadDocumentActivity, "Gambar maksimal 5 item")
            }
        }
    }

    private fun setupC1Dpr() {
        c1DprFiles = ArrayList()
//        c1PresidenImagesPart = ArrayList()
        c1DprAdapter = C1ImagesAdapter()
        c1DprAdapter.listener = object : C1ImagesAdapter.Listener {
            override fun onClickDelete(item: Image, adapterPosition: Int) {
                c1DprAdapter.deleteItem(adapterPosition)
            }
        }
        c1_dpr_list.layoutManager = LinearLayoutManager(this@UploadDocumentActivity)
        c1_dpr_list.adapter = c1DprAdapter
        add_c1_dpr_ri_button.setOnClickListener {
            if (c1DprAdapter.getListData().size < 5) {
                uploadType = "dpr"
                showImageChooserDialog()
            } else {
                ToastUtil.show(this@UploadDocumentActivity, "Gambar maksimal 5 item")
            }
        }
    }

    private fun setupC1Presiden() {
        c1PresidenFiles = ArrayList()
//        c1PresidenImagesPart = ArrayList()
        c1PresidenAdapter = C1ImagesAdapter()
        c1PresidenAdapter.listener = object : C1ImagesAdapter.Listener {
            override fun onClickDelete(item: Image, adapterPosition: Int) {
                c1PresidenAdapter.deleteItem(adapterPosition)
            }
        }
        c1_presiden_list.layoutManager = LinearLayoutManager(this@UploadDocumentActivity)
        c1_presiden_list.adapter = c1PresidenAdapter
        add_c1_presiden_button.setOnClickListener {
            if (c1PresidenAdapter.getListData().size < 2) {
                uploadType = "presiden"
                showImageChooserDialog()
            } else {
                ToastUtil.show(this@UploadDocumentActivity, "Gambar maksimal 2 item")
            }
        }
    }

    private fun showImageChooserDialog() {
        if (isHaveStorageAndCameraPermission()) {
            showIntentChooser()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PantauConstants.Permission.GET_IMAGE_PERMISSION, PantauConstants.RequestCode.RC_ASK_PERMISSIONS)
            }
        }
    }

    private fun isHaveStorageAndCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val storagePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val cameraPermission = checkSelfPermission(Manifest.permission.CAMERA)
            val writeExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            !(storagePermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED || writeExternalPermission != PackageManager.PERMISSION_GRANTED)
        } else {
            true
        }
    }

    private fun showIntentChooser() {
        val items = arrayOf<CharSequence>(getString(R.string.kamera), getString(R.string.galeri))
        val builder = AlertDialog.Builder(this@UploadDocumentActivity)
        builder.setTitle(getString(R.string.ambil_gambar_dari))
        builder.setItems(items) { dialog, item ->
            when (item) {
                0 -> takeFromCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openGallery() {
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        when (uploadType) {
            "presiden" -> startActivityForResult(Intent.createChooser(intentGallery, "Pilih"), 451)
            "dpr" -> startActivityForResult(Intent.createChooser(intentGallery, "Pilih"), 452)
            "dpd" -> startActivityForResult(Intent.createChooser(intentGallery, "Pilih"), 453)
            "dprd_prov" -> startActivityForResult(Intent.createChooser(intentGallery, "Pilih"), 454)
            "dprd_kab" -> startActivityForResult(Intent.createChooser(intentGallery, "Pilih"), 455)
            "suasana_tps" -> startActivityForResult(Intent.createChooser(intentGallery, "Pilih"), 456)
        }
    }

    private fun takeFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val cacheDir = cacheDir
        var file = File(cacheDir.absolutePath + "/image/")
        if (!file.isDirectory) {
            file.mkdir()
        }
        file = File(cacheDir, "image/" + System.currentTimeMillis().toString() + ".jpg")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this@UploadDocumentActivity, BuildConfig.APPLICATION_ID + ".provider", file))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        when (uploadType) {
            "presiden" -> {
                startActivityForResult(intent, 351)
                c1PresidenFiles.add(c1PresidenFiles.size, file)
            }
            "dpr" -> {
                startActivityForResult(intent, 352)
                c1DprFiles.add(c1DprFiles.size, file)
            }
            "dpd" -> {
                startActivityForResult(intent, 353)
                c1DpdFiles.add(c1DpdFiles.size, file)
            }
            "dprd_prov" -> {
                startActivityForResult(intent, 354)
                c1DprdProvFiles.add(c1DprdProvFiles.size, file)
            }
            "dprd_kab" -> {
                startActivityForResult(intent, 355)
                c1DprdKabFiles.add(c1DprdKabFiles.size, file)
            }
            "suasana_tps" -> {
                startActivityForResult(intent, 356)
                suasanaTpsFiles.add(suasanaTpsFiles.size, file)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PantauConstants.RequestCode.RC_ASK_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showIntentChooser()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == 351) {
                try {
                    ImageUtil.compressImage(this, c1PresidenFiles[c1PresidenFiles.size - 1], 2, object : ImageUtil.CompressorListener {
                        override fun onSuccess(file: File) {
//                            proceedCamera(file)?.let {
//                                c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                            }
                            c1PresidenAdapter.addItem(Image(file) as ItemModel)
                        }

                        override fun onFailed(throwable: Throwable) {
                            showError(throwable)
                            dismissProgressDialog()
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    dismissProgressDialog()
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == 451) {
                if (data != null) {
                    try {
//                        proceedGallery(ImageChooserTools.proccedImageFromStorage(
//                            data,
//                            this@UploadDocumentActivity
//                        ))?.let {
//                            c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                        }
                        c1PresidenAdapter.addItem(
                            Image(ImageChooserTools.proccedImageFromStorage(data, this@UploadDocumentActivity)) as ItemModel
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == 352) {
                try {
                    ImageUtil.compressImage(this, c1DprFiles[c1DprFiles.size - 1], 2, object : ImageUtil.CompressorListener {
                        override fun onSuccess(file: File) {
//                            proceedCamera(file)?.let {
//                                c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                            }
                            c1DprAdapter.addItem(Image(file) as ItemModel)
                        }

                        override fun onFailed(throwable: Throwable) {
                            showError(throwable)
                            dismissProgressDialog()
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    dismissProgressDialog()
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == 452) {
                if (data != null) {
                    try {
//                        proceedGallery(ImageChooserTools.proccedImageFromStorage(
//                            data,
//                            this@UploadDocumentActivity
//                        ))?.let {
//                            c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                        }
                        c1DprAdapter.addItem(
                            Image(ImageChooserTools.proccedImageFromStorage(data, this@UploadDocumentActivity)) as ItemModel
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == 353) {
                try {
                    ImageUtil.compressImage(this, c1DpdFiles[c1DpdFiles.size - 1], 2, object : ImageUtil.CompressorListener {
                        override fun onSuccess(file: File) {
//                            proceedCamera(file)?.let {
//                                c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                            }
                            c1DpdAdapter.addItem(Image(file) as ItemModel)
                        }

                        override fun onFailed(throwable: Throwable) {
                            showError(throwable)
                            dismissProgressDialog()
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    dismissProgressDialog()
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == 453) {
                if (data != null) {
                    try {
//                        proceedGallery(ImageChooserTools.proccedImageFromStorage(
//                            data,
//                            this@UploadDocumentActivity
//                        ))?.let {
//                            c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                        }
                        c1DpdAdapter.addItem(
                            Image(ImageChooserTools.proccedImageFromStorage(data, this@UploadDocumentActivity)) as ItemModel
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == 354) {
                try {
                    ImageUtil.compressImage(this, c1DprdProvFiles[c1DprdProvFiles.size - 1], 2, object : ImageUtil.CompressorListener {
                        override fun onSuccess(file: File) {
//                            proceedCamera(file)?.let {
//                                c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                            }
                            c1DprdProvAdapter.addItem(Image(file) as ItemModel)
                        }

                        override fun onFailed(throwable: Throwable) {
                            showError(throwable)
                            dismissProgressDialog()
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    dismissProgressDialog()
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == 454) {
                if (data != null) {
                    try {
//                        proceedGallery(ImageChooserTools.proccedImageFromStorage(
//                            data,
//                            this@UploadDocumentActivity
//                        ))?.let {
//                            c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                        }
                        c1DprdProvAdapter.addItem(
                            Image(ImageChooserTools.proccedImageFromStorage(data, this@UploadDocumentActivity)) as ItemModel
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == 355) {
                try {
                    ImageUtil.compressImage(this, c1DprdKabFiles[c1DprdKabFiles.size - 1], 2, object : ImageUtil.CompressorListener {
                        override fun onSuccess(file: File) {
//                            proceedCamera(file)?.let {
//                                c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                            }
                            c1DprdKabAdapter.addItem(Image(file) as ItemModel)
                        }

                        override fun onFailed(throwable: Throwable) {
                            showError(throwable)
                            dismissProgressDialog()
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    dismissProgressDialog()
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == 455) {
                if (data != null) {
                    try {
//                        proceedGallery(ImageChooserTools.proccedImageFromStorage(
//                            data,
//                            this@UploadDocumentActivity
//                        ))?.let {
//                            c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                        }
                        c1DprdKabAdapter.addItem(
                            Image(ImageChooserTools.proccedImageFromStorage(data, this@UploadDocumentActivity)) as ItemModel
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == 356) {
                try {
                    ImageUtil.compressImage(this, suasanaTpsFiles[suasanaTpsFiles.size - 1], 2, object : ImageUtil.CompressorListener {
                        override fun onSuccess(file: File) {
//                            proceedCamera(file)?.let {
//                                c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                            }
                            suasanaTpsAdapter.addItem(Image(file) as ItemModel)
                        }

                        override fun onFailed(throwable: Throwable) {
                            showError(throwable)
                            dismissProgressDialog()
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    dismissProgressDialog()
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == 456) {
                if (data != null) {
                    try {
//                        proceedGallery(ImageChooserTools.proccedImageFromStorage(
//                            data,
//                            this@UploadDocumentActivity
//                        ))?.let {
//                            c1PresidenImagesPart.add(c1PresidenImagesPart.size, it)
//                        }
                        suasanaTpsAdapter.addItem(
                            Image(ImageChooserTools.proccedImageFromStorage(data, this@UploadDocumentActivity)) as ItemModel
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    ToastUtil.show(this@UploadDocumentActivity, getString(R.string.failed_load_image_alert))
                }
            }
        }
    }

    private fun proceedGallery(newFile: File?): MultipartBody.Part? {
        val type: String?
        val extension = MimeTypeMap.getFileExtensionFromUrl(newFile?.absolutePath)
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        var reqFile: RequestBody? = null
        type?.let { it ->
            newFile?.let { it1 ->
                reqFile = RequestBody.create(MediaType.parse(it), it1)
            }
        }
        var file: MultipartBody.Part? = null
        reqFile?.let {
            file = MultipartBody.Part.createFormData("file", newFile?.name, it)
        }
        return file
    }

    private fun proceedCamera(imageFile: File): MultipartBody.Part? {
        val type: String
        val extension = MimeTypeMap.getFileExtensionFromUrl(imageFile.absolutePath)
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
        val reqFile = RequestBody.create(MediaType.parse(type), imageFile)
        val newFile = MultipartBody.Part.createFormData("file", imageFile.name, reqFile)
        return newFile
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
