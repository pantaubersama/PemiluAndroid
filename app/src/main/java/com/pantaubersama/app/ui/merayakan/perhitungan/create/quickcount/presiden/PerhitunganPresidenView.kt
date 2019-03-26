package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tps.realcount.RealCount

interface PerhitunganPresidenView : BaseView {
    fun onSuccessVoteCandidateCount()
    fun showFailedSaveDataAlert()
    fun bindRealCount(realCount: RealCount?)
    fun bindCounter(validCount: Int, invalidCount: Int, allCount: Int)
    fun showFailedGetRealCountAlert(message: String?)
}