package com.pantaubersama.app.data.interactors

import android.net.Uri
import android.webkit.MimeTypeMap
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pantaubersama.app.data.db.AppDB
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.data.model.createdat.CreatedAtInWord
import com.pantaubersama.app.data.model.tps.* // ktlint-disable
import com.pantaubersama.app.data.model.tps.image.Image
import com.pantaubersama.app.data.model.tps.image.ImageLocalModel
import com.pantaubersama.app.data.model.tps.image.ImageDoc
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.data.remote.APIWrapper
import com.pantaubersama.app.utils.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File

class TPSInteractor @Inject constructor(
    private val apiWrapper: APIWrapper,
    private val rxSchedulers: RxSchedulers,
    private val appDB: AppDB,
    private val dataCache: DataCache
) {
    private var gson: Gson = GsonBuilder().setFieldNamingPolicy(com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    fun getProvinces(): Single<MutableList<Province>> {
        return if (appDB.getProvinceDao().loadProvinces().size != 0) {
            Single.fromCallable {
                appDB.getProvinceDao().loadProvinces()
            }
        } else {
            apiWrapper.getPantauApi().getProvinces()
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
                .map { it.data.provinces }
                .doOnSuccess {
                    appDB.getProvinceDao().saveProvinces(it)
                }
        }
    }

    fun getRegencies(provinceId: Int): Single<MutableList<Regency>> {
        return apiWrapper.getPantauApi().getRegencies(provinceId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data.regencies
            }
    }

    fun getDistricts(regencyId: Int): Single<MutableList<District>> {
        return apiWrapper.getPantauApi().getDistricts(regencyId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data.districts
            }
    }

    fun getVillages(districtId: Int): Single<MutableList<Village>> {
        return apiWrapper.getPantauApi().getVillages(districtId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data.villages
            }
    }

    fun saveTPS(
        tpsNumber: Int,
        selectedProvince: Province,
        selectedRegency: Regency,
        selectedDistrict: District,
        selectedVillage: Village,
        lat: Double,
        long: Double
    ): Single<TPS> {
        var newId: Int = 0

        appDB.getTPSDAO().loadTPS().forEachIndexed { i, item ->
            newId = i + 1
        }

        val tps = TPS(
            newId.toString(),
            tpsNumber,
            selectedProvince,
            selectedRegency,
            selectedDistrict,
            selectedVillage,
            lat,
            long,
            "draft",
            "",
            CreatedAtInWord("", "", ""),
            dataCache.loadUserProfile()
        )
        appDB.getTPSDAO().saveTPS(tps)
        return Single.just(tps)
    }

    fun getMyTpses(page: Int, perPage: Int): Single<MutableList<TPS>> {
        return apiWrapper.getPantauApi()
                .getMyTPSes(
                    page,
                    perPage,
                    dataCache.loadUserProfile().id
                )
                .map {
                    it.tpsData.tpses
                }
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
    }

    fun deleteTps(tps: TPS): Completable {
        return when (tps.status) {
            "published" -> apiWrapper.getPantauApi().deleteTPS(tps.id)
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
            "draft" -> Completable.fromCallable {
                appDB.getTPSDAO().deleteTPS(tps)
            }
                .doOnComplete {
                    appDB.getRealCountDao().getRealCounts(tps.id).forEach {
                        appDB.getRealCountDao().deleteRealCount(it)
                    }
                }
            else -> Completable.fromCallable {
                appDB.getTPSDAO().deleteTPS(tps)
            }
                .doOnComplete {
                    appDB.getRealCountDao().getRealCounts(tps.id).forEach {
                        appDB.getRealCountDao().deleteRealCount(it)
                    }
                }
        }
    }

    fun updateTps(
        tpsId: String,
        tpsNumber: Int,
        status: String
    ): Completable {
        when (status) {
            "published" -> return apiWrapper.getPantauApi().updateTPS(
                tpsId,
                tpsNumber
            )
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
            "draft" -> {
                val tps = appDB.getTPSDAO().getTps(tpsId)
                tps.tps = tpsNumber
                return Completable.fromCallable {
                    appDB.getTPSDAO().updateTps(tps)
                }
            }
            else -> {
                val tps = appDB.getTPSDAO().getTps(tpsId)
                tps.tps = tpsNumber
                return Completable.fromCallable {
                    appDB.getTPSDAO().updateTps(tps)
                }
            }
        }
    }

    fun getLocalTpses(): MutableList<TPS> {
        return appDB.getTPSDAO().loadTPS()
    }

    fun getTps(tpsId: String): TPS {
        return appDB.getTPSDAO().getTps(tpsId)
    }

    fun createSandboxTps(): Completable {
        var newId: Int = 0

        appDB.getTPSDAO().loadTPS().forEachIndexed { i, item ->
            newId = i + 1
        }
        return Completable.fromCallable {
            val province = "{\n" +
                "        \"id\": 34,\n" +
                "        \"code\": 34,\n" +
                "        \"name\": \"DAERAH ISTIMEWA YOGYAKARTA\",\n" +
                "        \"level\": 1,\n" +
                "        \"domain_name\": \"yogyakartaprov\",\n" +
                "        \"id_wilayah\": 41863\n" +
                "      }"
            val regency = "{\n" +
                "        \"id\": 3404,\n" +
                "        \"province_id\": 34,\n" +
                "        \"code\": 3404,\n" +
                "        \"name\": \"SLEMAN\",\n" +
                "        \"level\": 2,\n" +
                "        \"domain_name\": \"slemankab\",\n" +
                "        \"id_wilayah\": 42221,\n" +
                "        \"id_parent\": 41863\n" +
                "      }"
            val district = "{\n" +
                "        \"id\": 340407,\n" +
                "        \"code\": 340407,\n" +
                "        \"regency_code\": 3404,\n" +
                "        \"name\": \"DEPOK\",\n" +
                "        \"id_parent\": 42221,\n" +
                "        \"id_wilayah\": 42259,\n" +
                "        \"level\": 3\n" +
                "      }"
            val village = "{\n" +
                "        \"id\": 3404072003,\n" +
                "        \"code\": 3404072003,\n" +
                "        \"district_code\": 340407,\n" +
                "        \"name\": \"Condongcatur\"\n" +
                "      }"
            val tps = TPS(
                newId.toString(),
                1,
                gson.fromJson(province, Province::class.java),
                gson.fromJson(regency, Regency::class.java),
                gson.fromJson(district, District::class.java),
                gson.fromJson(village, Village::class.java),
                -7.752120,
                110.405296,
                "sandbox",
                "",
                CreatedAtInWord("", "", ""),
                EMPTY_PROFILE
            )
            appDB.getTPSDAO().saveTPS(tps)
        }
            .doOnComplete { dataCache.setCreatedSandbox() }
    }

    fun isSandboxTpsCreated(): Boolean {
        return dataCache.isSandboxCreated()
    }

    fun saveImageDoc(
        tpsId: String,
        presiden: MutableList<ImageLocalModel>,
        dpr: MutableList<ImageLocalModel>,
        dpd: MutableList<ImageLocalModel>,
        dprdProv: MutableList<ImageLocalModel>,
        dprdKab: MutableList<ImageLocalModel>,
        suasanaTps: MutableList<ImageLocalModel>
    ): Completable {
        if (appDB.getImagesDao().getImage(tpsId) != null) {
            val imageDoc = appDB.getImagesDao().getImage(tpsId)
            imageDoc?.presiden = presiden
            imageDoc?.dpr = dpr
            imageDoc?.dpd = dpd
            imageDoc?.dprdProv = dprdProv
            imageDoc?.dprdKab = dprdKab
            imageDoc?.suasanaTps = suasanaTps
            return Completable.fromCallable {
                imageDoc?.let {
                    appDB.getImagesDao().updateImage(it)
                }
            }
        } else {
            var newId: Int = 0
            appDB.getImagesDao().getImages().forEachIndexed { index, image ->
                newId = index + 1
            }
            return Completable.fromCallable {
                appDB.getImagesDao().saveImage(
                    ImageDoc(newId.toString(), tpsId, presiden, dpr, dpd, dprdProv, dprdKab, suasanaTps)
                )
            }
        }
    }

    fun getImages(tpsId: String): ImageDoc? {
        return appDB.getImagesDao().getImage(tpsId)
    }

    fun uploadTps(tpsId: String): Single<TPS> {
        val tps = appDB.getTPSDAO().getTps(tpsId)
        return apiWrapper.getPantauApi().uploadTps(
            tps.tps,
            tps.province.code,
            tps.regency.code,
            tps.district.code,
            tps.village.code,
            tps.latitude,
            tps.longitude
        )
            .map { it.tpsData.tps }
    }

    fun uploadRealCount(tpsId: String, dbTpsId: String, realCountType: String): Completable? {
        val realCount = appDB.getRealCountDao().getRealCount(dbTpsId, realCountType)
        return if (realCount != null) {
            realCount.hitungRealCountId = tpsId
            val realCountJson = gson.toJson(realCount)
            val realCountBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), realCountJson)
            apiWrapper.getPantauApi().uploadRealCount(realCountBody)
        } else {
            null
        }
    }

    fun uploadC1(apiTpsId: String, dbTpsId: String, c1Type: String): Completable? {
        val c1 = appDB.getC1Dao().getC1(dbTpsId, c1Type)
        return if (c1 != null) {
            c1.tpsId = apiTpsId
            val c1Json = gson.toJson(c1)
            Timber.d(c1Json.toString())
            val c1Body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), c1Json)
            apiWrapper.getPantauApi().uploadC1Form(c1Body)
        } else {
            null
        }
    }

    private fun proceedImage(imageFile: File): MultipartBody.Part {
        val type: String
        val extension = MimeTypeMap.getFileExtensionFromUrl(imageFile.absolutePath)
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
        val reqFile = RequestBody.create(MediaType.parse(type), imageFile)
        val newFile = MultipartBody.Part.createFormData("file", imageFile.name, reqFile)
        return newFile
    }

    fun getImagesWithType(dbTpsId: String, imagesUploadType: String): MutableList<ImageLocalModel>? {
        when (imagesUploadType) {
            "c1_presiden" -> return appDB.getImagesDao().getImage(dbTpsId)?.presiden
            "c1_dpr_ri" -> return appDB.getImagesDao().getImage(dbTpsId)?.dpr
            "c1_dpd" -> return appDB.getImagesDao().getImage(dbTpsId)?.dpd
            "c1_dprd_provinsi" -> return appDB.getImagesDao().getImage(dbTpsId)?.dprdProv
            "c1_dprd_kabupaten" -> return appDB.getImagesDao().getImage(dbTpsId)?.dprdKab
            "suasana_tps" -> return appDB.getImagesDao().getImage(dbTpsId)?.suasanaTps
            else -> return null
        }
    }

    fun uploadImage(apiTpsId: String, imagesUploadType: String, uri: String): Completable {
        return apiWrapper.getPantauApi().uploadImage(apiTpsId, imagesUploadType, proceedImage(File(Uri.parse(uri).path)))
    }

    fun publishRealCount(apiTpsId: String, dbTpsId: String): Completable {
        val tps = appDB.getTPSDAO().getTps(dbTpsId)
        return apiWrapper.getPantauApi().publishRealCount(apiTpsId)
            .doOnComplete {
                when (tps.status) {
                    "draft" -> {
                        appDB.getRealCountDao().getRealCounts(tps.id).forEach {
                            appDB.getRealCountDao().deleteRealCount(it)
                        }
                        appDB.getC1Dao().getC1s(tps.id).forEach {
                            appDB.getC1Dao().deleteC1(it)
                        }
                        appDB.getImagesDao().getImages(tps.id).forEach {
                            appDB.getImagesDao().deleteImage(it)
                        }
                        appDB.getTPSDAO().deleteTPS(tps)
                    }
                    else -> {
                        appDB.getRealCountDao().getRealCounts(tps.id).forEach {
                            appDB.getRealCountDao().deleteRealCount(it)
                        }
                        appDB.getC1Dao().getC1s(tps.id).forEach {
                            appDB.getC1Dao().deleteC1(it)
                        }
                        appDB.getImagesDao().getImages(tps.id).forEach {
                            appDB.getImagesDao().deleteImage(it)
                        }
                        appDB.getTPSDAO().deleteTPS(tps)
                    }
                }
            }
    }

    fun getTpses(page: Int, perPage: Int, villageCode: Long): Single<MutableList<TPS>> {
        return apiWrapper.getPantauApi().getTPSes(page, perPage, villageCode)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.tpsData.tpses
            }
    }

    fun getImagesFromApi(tpsId: String): Single<MutableList<Image>> {
        return apiWrapper.getPantauApi().getImages(tpsId)
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.mainThread())
            .map {
                it.data.image
            }
    }
}