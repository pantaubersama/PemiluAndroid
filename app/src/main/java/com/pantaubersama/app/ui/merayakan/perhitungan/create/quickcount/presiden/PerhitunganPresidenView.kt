package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import com.pantaubersama.app.base.BaseView

interface PerhitunganPresidenView : BaseView {
    fun onSuccessVoteCandidateCount(validCount: Int, invalidCount: Int, allCount: Int)
}