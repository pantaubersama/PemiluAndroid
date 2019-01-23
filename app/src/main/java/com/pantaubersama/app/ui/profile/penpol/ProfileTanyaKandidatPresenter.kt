package com.pantaubersama.app.ui.profile.penpol

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.TanyaKandidatInteractor
import javax.inject.Inject

class ProfileTanyaKandidatPresenter @Inject constructor(
    private val tanyaKandidatInteractor: TanyaKandidatInteractor,
    private val profileInteractor: ProfileInteractor
) : BasePresenter<ProfileTanyaKandidatView>() {
    var perPage = 20

    fun getTanyaKandidatList(page: Int) {
        if (page == 1) {
            view?.showLoading()
        }

        disposables.add(
            tanyaKandidatInteractor.getMyTanyaKandidatList(page, perPage)
                .subscribe(
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            if (it?.questions != null) {
                                if (it.questions.size != 0) {
                                    view?.bindDataTanyaKandidat(it.questions)
                                } else {
                                    view?.showEmptyDataAlert()
                                }
                            } else {
                                view?.showFailedGetDataAlert()
                            }
                        } else {
                            view?.showFailedGetDataAlert()
                        }
                    },
                    {
                        view?.dismissLoading()
                        view?.showFailedGetDataAlert()
                        view?.showError(it)
                    }
                )
        )
//        val tanyaKandidatList: MutableList<Pertanyaan> = ArrayList()
//
//        for (i in 1..25) {
//            val data = Pertanyaan()
//            data.question = "Apakah ini angka $i ?"
//            data.user?.name = "Andi $i ?"
//            data.user?.bio = "I love Indonesia"
//            data.createdAtInWord = "A seconds ago"
//            data.upVoteCount = 100
//            tanyaKandidatList.add(data)
//        }
//
//        if (tanyaKandidatList.size != 0) {
//            view?.dismissLoading()
//            view?.bindDataTanyaKandidat(tanyaKandidatList)
//        } else {
//            view?.dismissLoading()
//            view?.showEmptyDataAlert()
//        }
    }

    fun upVoteQuestion(id: String, questionCalass: String, isLiked: Boolean, position: Int) {
        disposables.add(
            tanyaKandidatInteractor
                .upVoteQuestion(
                    id,
                    questionCalass
                )
                .subscribe(
                    {
                        view?.onItemUpVoted()
                    },
                    {
                        view?.showError(it)
                        view?.onFailedUpVoteItem(isLiked, position)
                    }
                )
        )
    }

    fun deleteItem(id: String, position: Int) {
        disposables.add(
            tanyaKandidatInteractor
                .deleteQuestions(id)
                .subscribe(
                    {
                        view?.onItemDeleted(position)
                    },
                    {
                        view?.showError(it)
                        view?.showFailedDeleteItemAlert()
                    }
                )
        )
    }

    fun unVoteQuestion(id: String, className: String, liked: Boolean, position: Int) {
        disposables.add(
            tanyaKandidatInteractor
                .unVoteQuestion(id, className)
                .subscribe(
                    {
                        view?.onItemUpVoted()
                    },
                    {
                        view?.showError(it)
                        view?.onFailedUpVoteItem(liked, position)
                    }
                )
        )
    }

    fun getProfile() {
        view?.bindProfile(profileInteractor.getProfile())
    }

    fun getUserTanyaKandidat(page: Int, userId: String) {
        disposables.add(
            tanyaKandidatInteractor.getUserTanyaKandidatList(page, perPage, userId)
                .subscribe(
                    {
                        if (page == 1) {
                            view?.dismissLoading()
                            if (it?.questions != null) {
                                if (it.questions.size != 0) {
                                    view?.bindDataTanyaKandidat(it.questions)
                                } else {
                                    view?.showEmptyDataAlert()
                                }
                            } else {
                                view?.showFailedGetDataAlert()
                            }
                        } else {
                            view?.showFailedGetDataAlert()
                        }
                    },
                    {
                        view?.dismissLoading()
                        view?.showFailedGetDataAlert()
                        view?.showError(it)
                    }
                )
        )
    }
}