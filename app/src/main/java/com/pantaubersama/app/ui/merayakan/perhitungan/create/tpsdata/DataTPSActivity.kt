package com.pantaubersama.app.ui.merayakan.perhitungan.create.tpsdata

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.tps.District
import com.pantaubersama.app.data.model.tps.Province
import com.pantaubersama.app.data.model.tps.Regency
import com.pantaubersama.app.data.model.tps.Village
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.perhitunganhome.PerhitunganMainActivity
import com.pantaubersama.app.ui.widget.ConfirmationDialog
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_ASK_PERMISSIONS
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_data_tps.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.util.Locale
import javax.inject.Inject

class DataTPSActivity : BaseActivity<DataTPSPresenter>(), DataTPSView {
    var locationManager: LocationManager? = null
    private var permission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var geocoder: Geocoder? = null
    // location
    private lateinit var addresses: List<Address>
    private var lat: Float = 0f
    private var long: Float = 0f

    // provinces
    private lateinit var provinceNames: MutableList<String>
    private lateinit var provincesAdapter: ArrayAdapter<String>
    private lateinit var selectedProvince: Province
    // regencies
    private lateinit var regencyNames: MutableList<String>
    private lateinit var regenciesAdapter: ArrayAdapter<String>
    private lateinit var selectedRegency: Regency
    // districts
    private lateinit var districtNames: MutableList<String>
    private lateinit var districtsAdapter: ArrayAdapter<String>
    private lateinit var selectedDistrict: District
    // villages
    private lateinit var villageNames: MutableList<String>
    private lateinit var villagesAdapter: ArrayAdapter<String>
    private lateinit var selectedVillage: Village

    @Inject
    override lateinit var presenter: DataTPSPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

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
        setupProvincesDropdown()
        setupRegenciesDropdown()
        setupDistrictsDropdown()
        setupVillagesDropdown()
        presenter.getProvincesData()
    }

    private fun setupVillagesDropdown() {
        villageNames = ArrayList()
        villagesAdapter = ArrayAdapter<String>(
            this@DataTPSActivity,
            R.layout.default_collapsed_spinner_item,
            villageNames
        )
        villagesAdapter.setDropDownViewResource(R.layout.default_expanded_spinner_item)
        villages_dropdown.adapter = villagesAdapter
        villages_dropdown.isEnabled = false
    }

    private fun setupDistrictsDropdown() {
        districtNames = ArrayList()
        districtsAdapter = ArrayAdapter<String>(
            this@DataTPSActivity,
            R.layout.default_collapsed_spinner_item,
            districtNames
        )
        districtsAdapter.setDropDownViewResource(R.layout.default_expanded_spinner_item)
        districts_dropdown.adapter = districtsAdapter
        districts_dropdown.isEnabled = false
    }

    private fun setupRegenciesDropdown() {
        regencyNames = ArrayList()
        regenciesAdapter = ArrayAdapter<String>(
            this@DataTPSActivity,
            R.layout.default_collapsed_spinner_item,
            regencyNames
        )
        regenciesAdapter.setDropDownViewResource(R.layout.default_expanded_spinner_item)
        regencies_dropdown.adapter = regenciesAdapter
        regencies_dropdown.isEnabled = false
    }

    private fun setupProvincesDropdown() {
        provinceNames = ArrayList()
        provincesAdapter = ArrayAdapter<String>(
            this@DataTPSActivity,
            R.layout.default_collapsed_spinner_item,
            provinceNames
        )
        provincesAdapter.setDropDownViewResource(R.layout.default_expanded_spinner_item)
        provinces_dropdown.adapter = provincesAdapter
    }

    override fun showProvincesLoading() {
        provinces_loading.visibility = View.VISIBLE
    }

    override fun dismissProvincesLoading() {
        provinces_loading.visibility = View.GONE
    }

    override fun showFailedGetProvincesAlert() {
        ToastUtil.show(this@DataTPSActivity, "Gagal memuat Provinsi")
    }

    override fun bindProvincesToSpinner(provinces: MutableList<Province>) {
        provinceNames.clear()
        provinceNames.add(0, "Pilih Provinsi: ")
        provinces.forEach {
            provinceNames.add(it.name)
        }
        provincesAdapter.notifyDataSetChanged()
        provinces_dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position != 0) {
                    provinces_empty_alert.visibility = View.GONE
                    selectedProvince = provinces[position - 1]
                    presenter.getRegenciesData(provinces[position - 1].id)
                }
            }
        }
    }

    override fun showRegenciesLoading() {
        regencies_loading.visibility = View.VISIBLE
    }

    override fun dismissRegenciesLoading() {
        regencies_loading.visibility = View.GONE
    }

    override fun showFailedGetRegenciesAlert() {
        ToastUtil.show(this@DataTPSActivity, "Gagal memuat Kabupaten")
    }

    override fun bindRegenciesToSpinner(regencies: MutableList<Regency>) {
        regencyNames.clear()
        regencyNames.add(0, "Pilih Kabupaten: ")
        regencies.forEach {
            regencyNames.add(it.name)
        }
        regenciesAdapter.notifyDataSetChanged()
        regencies_dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position != 0) {
                    regencies_empty_alert.visibility = View.GONE
                    selectedRegency = regencies[position - 1]
                    presenter.getDistrictsData(regencies[position - 1].id)
                }
            }
        }
        regencies_dropdown.isEnabled = true
    }

    override fun showDistrictsLoading() {
        districts_loading.visibility = View.VISIBLE
    }

    override fun dismissDistrictsLoading() {
        districts_loading.visibility = View.GONE
    }

    override fun showFailedGetDistrictsAlert() {
        ToastUtil.show(this@DataTPSActivity, "Gagal memuat Kecamatan")
    }

    override fun bindDistrictsToSpinner(districts: MutableList<District>) {
        districtNames.clear()
        districtNames.add(0, "Pilih Kecamatan: ")
        districts.forEach {
            districtNames.add(it.name)
        }
        districtsAdapter.notifyDataSetChanged()
        districts_dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position != 0) {
                    districts_empty_alert.visibility = View.GONE
                    selectedDistrict = districts[position - 1]
                    presenter.getVillagesData(districts[position - 1].id)
                }
            }
        }
        districts_dropdown.isEnabled = true
    }

    override fun showVillagesLoading() {
        villages_loading.visibility = View.VISIBLE
    }

    override fun dismissVillagesLoading() {
        villages_loading.visibility = View.GONE
    }

    override fun bindVillagesToSpinner(villages: MutableList<Village>) {
        villageNames.clear()
        villageNames.add(0, "Pilih Kelurahan/Desa: ")
        villages.forEach {
            villageNames.add(it.name)
        }
        villagesAdapter.notifyDataSetChanged()
        villages_dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position != 0) {
                    selectedVillage = villages[position - 1]
                    villages_empty_alert.visibility = View.GONE
                }
            }
        }
        villages_dropdown.isEnabled = true
    }

    override fun showFailedGetVillagesAlert() {
        ToastUtil.show(this@DataTPSActivity, "Gagal memuat Kelurahan")
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
                            geocoder?.getFromLocation(location.latitude, location.longitude, 1)?.let {
                                addresses = it
                            }
                            val address = addresses.get(0).getAddressLine(0)
                            location_empty_alert.visibility = View.GONE
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

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.done_action -> {
                validateInput()
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun validateInput() {
        if (tps_number_field.text.isEmpty()) {
            tps_number_field.error = "Nomor TPS tidak boleh kosong"
        } else {
            if (provinces_dropdown.selectedItemPosition == 0) {
                provinces_empty_alert.visibility = View.VISIBLE
            } else {
                if (regencies_dropdown.selectedItemPosition == 0) {
                    regencies_empty_alert.visibility = View.VISIBLE
                } else {
                    if (districts_dropdown.selectedItemPosition == 0) {
                        districts_empty_alert.visibility = View.VISIBLE
                    } else {
                        if (villages_dropdown.selectedItemPosition == 0) {
                            villages_empty_alert.visibility = View.VISIBLE
                        } else {
                            if (address_text.text.isEmpty()) {
                                location_empty_alert.visibility = View.VISIBLE
                            } else {
                                presenter.saveDataTPS(
                                    tps_number_field.text.toString().toInt(),
                                    selectedProvince,
                                    selectedRegency,
                                    selectedDistrict,
                                    selectedVillage,
                                    lat,
                                    long
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onSuccessSaveTPS() {
        startActivity(Intent(this@DataTPSActivity, PerhitunganMainActivity::class.java))
        finish()
    }
}