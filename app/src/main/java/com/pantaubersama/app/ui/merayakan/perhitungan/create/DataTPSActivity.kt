package com.pantaubersama.app.ui.merayakan.perhitungan.create

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.* //ktlint-disable
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.merayakan.perhitungan.create.perhitunganhome.PerhitunganMainActivity
import com.pantaubersama.app.ui.widget.ConfirmationDialog
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_ASK_PERMISSIONS
import kotlinx.android.synthetic.main.activity_data_tps.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.util.Locale

class DataTPSActivity : CommonActivity() {
    var locationManager: LocationManager? = null
    private var permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var geocoder: Geocoder? = null
    private var addresses: List<Address>? = null
//    private val locationListener = object : LocationListener {
//        override fun onLocationChanged(location: Location) {
//            location_progressbar.visibility = View.GONE
//            address_text.visibility = View.VISIBLE
//            addresses = geocoder?.getFromLocation(location.latitude, location.longitude, 1)
//            val address = addresses?.get(0)?.getAddressLine(0)
//            address_text.text = address
//        }
//
//        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
//        }
//
//        override fun onProviderEnabled(p0: String?) {
//        }
//
//        override fun onProviderDisabled(p0: String?) {
//            ToastUtil.show(this@DataTPSActivity, "Hidupkan GPS kamu")
//        }
//    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_data_tps
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Data TPS", R.color.white, 4f)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        geocoder = Geocoder(this, Locale.getDefault())
        getLocationPermission()
    }

    @AfterPermissionGranted(RC_ASK_PERMISSIONS)
    private fun getLocationPermission() {
        if (EasyPermissions.hasPermissions(this, *permission)) {
            update_location_button.setOnClickListener {
                if (isLocationEnabled(this@DataTPSActivity)) {
                    location_progressbar.visibility = View.VISIBLE
                    address_text.visibility = View.GONE
                    try {
//                    locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
                        val location: Location? = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                        location_progressbar.visibility = View.GONE
                        address_text.visibility = View.VISIBLE
                        if (location != null) {
                            addresses = geocoder?.getFromLocation(location.latitude, location.longitude, 1)
                            val address = addresses?.get(0)?.getAddressLine(0)
                            address_text.text = address
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    ConfirmationDialog.Builder()
                        .with(this@DataTPSActivity)
                        .setDialogTitle("Actifkan GPS")
                        .setAlert("Kami gagal menemukan lokasi kamu karena GPS kamu non-aktif. Mohon hidupkan GPS kamu.")
                        .setOkText("Aktifkan")
                        .setCancelText("Batal")
                        .addOkListener(object : ConfirmationDialog.DialogOkListener {
                            override fun onClickOk() {
                                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            }
                        })
                        .show()
                }
            }
        } else {
            requestPermission()
        }
    }

    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String
        try {
            locationMode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            return false
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF
    }

    private fun requestPermission() {
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(this, RC_ASK_PERMISSIONS, *permission)
                .setRationale(getString(R.string.izinkan_akses_lokasi))
                .setPositiveButtonText(getString(R.string.ok))
                .setNegativeButtonText(getString(R.string.batal))
                .build()
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        fun start(context: Context, requsetCode: Int? = null) {
            val intent = Intent(context, DataTPSActivity::class.java)
            if (requsetCode != null) {
                (context as Activity).startActivityForResult(intent, requsetCode)
            } else {
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.done_action -> {
                // sementara
                startActivity(Intent(this@DataTPSActivity, PerhitunganMainActivity::class.java))
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
