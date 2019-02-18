package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.kandidat.CandidateData
import com.pantaubersama.app.data.model.partai.* // ktlint-disable
import javax.inject.Inject

class PerhitunganDPRRIPresenter @Inject constructor() : BasePresenter<PerhitunganDPRRIView>() {
    fun getDPRRIData() {
        val candidate1: MutableList<CandidateData> = ArrayList()
        candidate1.add(CandidateData("Anwar", 1))
        candidate1.add(CandidateData("Jalu", 2))
        candidate1.add(CandidateData("Supardi B.A", 3))
        candidate1.add(CandidateData("Suryono B.A", 4))
        candidate1.add(CandidateData("Saerah Supandi", 5))

        val candidate2: MutableList<CandidateData> = ArrayList()
        candidate2.add(CandidateData("Tukiman S.E", 1))
        candidate2.add(CandidateData("Martowijowo A.Md", 2))
        candidate2.add(CandidateData("Suprono B.A", 3))
        candidate2.add(CandidateData("Janur Rzak Hamidi S.T", 4))
        candidate2.add(CandidateData("Siswoyo S.A.G", 5))

        val parties: MutableList<PoliticalParty> = ArrayList()
        parties.add(
            PoliticalParty(
                "0123",
                Image(Medium(""), MediumSquare(""), Thumbnail(""), ThumbnailSquare(""), "https://upload.wikimedia.org/wikipedia/id/9/9f/Pkb.jpg"),
                "Partai Kebangkitan Bangsa",
                1,
                candidate1
            )
        )
        parties.add(
            PoliticalParty(
                "0124",
                Image(Medium(""), MediumSquare(""), Thumbnail(""), ThumbnailSquare(""), "http://ppp.or.id/wp-content/uploads/2018/09/73.jpg"),
                "Partai Persatuan pembangunan",
                2,
                candidate2
            )
        )

        view?.bindData(parties)
    }
}