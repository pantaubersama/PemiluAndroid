package com.pantaubersama.app.ui.merayakan.rekapitulasi.tpslist

import com.google.gson.Gson
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.image.Image
import com.pantaubersama.app.data.model.tps.TPSData
import com.pantaubersama.app.data.model.user.EMPTY_INFORMANT
import com.pantaubersama.app.data.model.user.Profile
import javax.inject.Inject

class TPSListPresenter @Inject constructor() : BasePresenter<TPSListView>() {
    fun getTPSListData() {
        view?.showLoading()
        val profile1 = Profile("", "", "Suparti", "", "",
            false, false, null, 0, null, false,
            null, "", "", "", "",
            Image("https://m.media-amazon.com/images/M/MV5BODI1NTg2MzE0N15BMl5BanBnXkFtZTcwMTE5NDgxOA@@._V1_SY1000_CR0,0,666,1000_AL_.jpg"), EMPTY_INFORMANT)

        val data = "{\n" +
            "  \"created_at\": \"2019-03-05T06:30:25.107Z\",\n" +
            "  \"created_at_in_word\": {\n" +
            "    \"time_zone\": \"Asia/Jakarta\",\n" +
            "    \"iso_8601\": \"2019-03-05T13:30:25.107+07:00\",\n" +
            "    \"en\": \"2 minutes\",\n" +
            "    \"id\": \"2mnt\"\n" +
            "  },\n" +
            "  \"id\": \"1d241b9b-c0a1-4dc7-8977-0f2a4258499b\",\n" +
            "  \"tps\": 1,\n" +
            "  \"province\": {\n" +
            "        \"id\": 19,\n" +
            "        \"code\": 19,\n" +
            "        \"name\": \"KEPULAUAN BANGKA BELITUNG\",\n" +
            "        \"level\": 1,\n" +
            "        \"domain_name\": \"babelprov\",\n" +
            "        \"id_wilayah\": 24993\n" +
            "      },\n" +
            "  \"regency\": {\n" +
            "        \"id\": 3404,\n" +
            "        \"province_id\": 34,\n" +
            "        \"code\": 3404,\n" +
            "        \"name\": \"SLEMAN\",\n" +
            "        \"level\": 2,\n" +
            "        \"domain_name\": \"slemankab\",\n" +
            "        \"id_wilayah\": 42221,\n" +
            "        \"id_parent\": 41863\n" +
            "      },\n" +
            "  \"district\": {\n" +
            "        \"id\": 340401,\n" +
            "        \"code\": 340401,\n" +
            "        \"regency_code\": 3404,\n" +
            "        \"name\": \"GAMPING\",\n" +
            "        \"id_parent\": 42221,\n" +
            "        \"id_wilayah\": 42222,\n" +
            "        \"level\": 3\n" +
            "      },\n" +
            "  \"village\": {\n" +
            "        \"id\": 3404032001,\n" +
            "        \"code\": 3404032001,\n" +
            "        \"district_code\": 340403,\n" +
            "        \"name\": \"Sumberahayu\"\n" +
            "      },\n" +
            "  \"latitude\": \"37.422\",\n" +
            "  \"longitude\": \"-122.084\",\n" +
            "  \"status\": \"published\"\n" +
            "}"

        val tpsData = Gson().newBuilder().create().fromJson<TPSData>(data, TPSData::class.java)
//        tpsData.user = profile1

        val tpses: MutableList<TPSData> = ArrayList()
        tpses.add(tpsData)
        tpses.add(tpsData)
        tpses.add(tpsData)
        view?.dismissLoading()
        view?.bindPerhitungan(tpses)
    }
}