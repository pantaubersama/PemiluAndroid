package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.model.tps.RealCount

interface PerhitunganPresidenView : BaseView {
    fun onSuccessVoteCandidateCount(validCount: Int, invalidCount: Int, allCount: Int)
    fun showFailedSaveDataAlert()
    fun bindRealCount(realCount: RealCount?)
}