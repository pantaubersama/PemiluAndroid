package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidat
import javax.inject.Inject

class TanyaKandidatPresenter @Inject constructor() : BasePresenter<TanyaKandidatView>() {
    fun getDataKandidatList() {
        view?.showLoading()
        val tanyaKandidatList: MutableList<TanyaKandidat> = ArrayList()

        for (i in 1..25) {
            val data = TanyaKandidat()
            data.question = "Apakah ini angka $i ?"
            data.user?.name = "Andi $i ?"
            data.user?.bio = "I love Indonesia"
            data.createdAt = "A seconds ago"
            data.upvoteCount = 100
            tanyaKandidatList.add(data)
        }

        if (tanyaKandidatList.size != 0) {
            view?.dismissLoading()
            view?.bindDataTanyaKandidat(tanyaKandidatList)
        } else {
            view?.dismissLoading()
            view?.showEmptyDataAlert()
        }
    }
}