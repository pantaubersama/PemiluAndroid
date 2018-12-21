package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidat

class TanyaKandidatPresenter : BasePresenter<TanyaKandidatView>() {
    fun getTanyaKandidatList() {
        view?.showLoading()
        val tanyaKandidatList: MutableList<TanyaKandidat> = ArrayList()

        for (i in 1..25) {
            val data = TanyaKandidat()
            data.question = "Apakah ini angka $i ?"
            data.user?.name = "Andi $i ?"
            data.user?.bio = "I love Indonesia"
            data.createdAt = "A seconds ago"
            data.upVoteCount = 100
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