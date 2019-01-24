package com.pantaubersama.app.ui.penpol.tanyakandidat.detail

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.interactors.TanyaKandidatInteractor
import com.pantaubersama.app.utils.PantauConstants
import javax.inject.Inject

class DetailTanyaKandidatPresenter @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val interactor: TanyaKandidatInteractor
) : BasePresenter<DetailTanyaKandidatView>() {

    fun getData(questionId: String) {
        view?.showLoading()
        disposables.add(interactor.getTanyaKandidatById(questionId)
            .subscribe(
                {
                    view?.bindData(it!!)
                },
                {
                    it.printStackTrace()
                    view?.onFailedGetData(it)
                    view?.showError(it)
                }
            )
        )
    }

    fun upvoteQuestion(id: String, isLiked: Boolean) {
        disposables.add(
            interactor.upVoteQuestion(id, PantauConstants.TanyaKandidat.CLASS_NAME)
                .subscribe(
                    {
                        view?.onItemUpVoted()
                    },
                    {
                        view?.showError(it)
                        view?.onFailedUpVoteItem(isLiked)
                    }
                )
        )
    }

    fun reportQuestion(id: String) {
        disposables.add(
            interactor.reportQuestion(id, PantauConstants.TanyaKandidat.CLASS_NAME)
                .subscribe(
                    {
                        view?.showItemReportedAlert()
                    },
                    {
                        view?.showError(it)
                        view?.showFailedReportItem()
                    }
                )
        )
    }

    fun deleteItem(id: String) {
        disposables.add(
            interactor.deleteQuestions(id)
                .subscribe(
                    {
                        view?.onItemDeleted()
                    },
                    {
                        view?.showError(it)
                        view?.showFailedDeleteItemAlert()
                    }
                )
        )
    }

    fun unvoteQuestion(id: String, isLiked: Boolean) {
        disposables.add(
            interactor.unVoteQuestion(id, PantauConstants.TanyaKandidat.CLASS_NAME)
                .subscribe(
                    {
                        view?.onItemUpVoted()
                    },
                    {
                        view?.showError(it)
                        view?.onFailedUpVoteItem(isLiked)
                    }
                )
        )
    }

    fun getProfile() {
        view?.bindProfile(profileInteractor.getProfile())
    }
}