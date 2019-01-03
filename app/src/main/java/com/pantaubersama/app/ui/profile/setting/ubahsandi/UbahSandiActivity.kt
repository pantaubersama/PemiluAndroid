package com.pantaubersama.app.ui.profile.setting.ubahsandi

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_ubah_sandi.*
import timber.log.Timber
import javax.inject.Inject

class UbahSandiActivity : BaseActivity<UbahSandiPresenter>(), UbahSandiView {
    @Inject
    lateinit var profileInteractor: ProfileInteractor
    var passwordValid = false

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initInjection() {
        (application as BaseApp).createActivityComponent(this)?.inject(this)
    }

    override fun initPresenter(): UbahSandiPresenter? {
        return UbahSandiPresenter(profileInteractor)
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.title_ubah_sandi), R.color.white, 4f)
        setupPasswordField()
        onClickAction()
    }

    private fun setupPasswordField() {
        ubah_sandi_sandi_baru.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(string: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (string?.length!! < 8) {
                    ubah_sandi_sandi_baru.error = "Password minimal 8 karakter"
                }
            }
        })
        repeat_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(string: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (string.toString() != ubah_sandi_sandi_baru.text.toString()) {
                    repeat_password.error = "Password tidak cocok"
                }
            }
        })
    }

    override fun setLayout(): Int {
        return R.layout.activity_ubah_sandi
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun onClickAction() {
        ubah_sandi_ubah.setOnClickListener {
            if (ubah_sandi_sandi_baru.error == null) {
                if (repeat_password.error == null) {
                    if (ubah_sandi_lama.text.isNotEmpty()) {
                        presenter?.updatePassword(
                            ubah_sandi_sandi_baru.text.toString(),
                            repeat_password.text.toString()
                        )
                    } else {
                        ubah_sandi_lama.error = "Passowrd lama harus diisi"
                    }
                }
            }
        }
    }

    override fun showSuccessUpdatePasswordAlert() {
        ToastUtil.show(this@UbahSandiActivity, "Berhasil memperbarui password")
    }

    override fun finishActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun showFailedUpdatePasswordAlert() {
        ToastUtil.show(this@UbahSandiActivity, "Gagal memperbarui password")
    }

    override fun disableView() {
        ubah_sandi_ubah.isEnabled = false
        ubah_sandi_ubah.setBackgroundColor(ContextCompat.getColor(this@UbahSandiActivity, R.color.gray_dark_1))
        for (i in 0 until ubah_password_container.childCount) {
            val child = ubah_password_container.getChildAt(i)
            child.isEnabled = false
        }
    }

    override fun enableView() {
        ubah_sandi_ubah.isEnabled = true
        ubah_sandi_ubah.setBackgroundColor(ContextCompat.getColor(this@UbahSandiActivity, R.color.colorPrimary))
        for (i in 0 until ubah_password_container.childCount) {
            val child = ubah_password_container.getChildAt(i)
            child.isEnabled = true
        }
    }
}
