package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.dpr.DPRData
import com.pantaubersama.app.data.model.partai.* // ktlint-disable
import javax.inject.Inject

class PerhitunganDPRRIPresenter @Inject constructor() : BasePresenter<PerhitunganDPRRIView>() {
    fun getDPRRIData() {
        val dpr1: MutableList<DPRData> = ArrayList()
        dpr1.add(DPRData("Anwar", 1))
        dpr1.add(DPRData("Jalu", 2))
        dpr1.add(DPRData("Supardi B.A", 3))
        dpr1.add(DPRData("Suryono B.A", 4))
        dpr1.add(DPRData("Saerah Supandi", 5))

        val dpr2: MutableList<DPRData> = ArrayList()
        dpr2.add(DPRData("Tukiman S.E", 1))
        dpr2.add(DPRData("Martowijowo A.Md", 2))
        dpr2.add(DPRData("Suprono B.A", 3))
        dpr2.add(DPRData("Janur Rzak Hamidi S.T", 4))
        dpr2.add(DPRData("Siswoyo S.A.G", 5))

        val parties: MutableList<PoliticalParty> = ArrayList()
        parties.add(
            PoliticalParty(
                "0123",
                Image(Medium(""), MediumSquare(""), Thumbnail(""), ThumbnailSquare(""), "https://upload.wikimedia.org/wikipedia/id/9/9f/Pkb.jpg"),
                "Partai Kebangkitan Bangsa",
                1,
                dpr1
            )
        )
        parties.add(
            PoliticalParty(
                "0124",
                Image(Medium(""), MediumSquare(""), Thumbnail(""), ThumbnailSquare(""), "http://ppp.or.id/wp-content/uploads/2018/09/73.jpg"),
                "Partai Persatuan pembangunan",
                2,
                dpr2
            )
        )

        view?.bindData(parties)
    }
}