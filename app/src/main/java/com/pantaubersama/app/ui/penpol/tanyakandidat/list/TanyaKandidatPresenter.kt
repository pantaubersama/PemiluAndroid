package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.interactors.BannerInfoInteractor
import com.pantaubersama.app.data.interactors.TanyaKandidatInteractor
import com.pantaubersama.app.data.local.cache.DataCache
import com.pantaubersama.app.utils.PantauConstants
import javax.inject.Inject

class TanyaKandidatPresenter @Inject constructor(
    private val dataCache: DataCache,
    private val tanyaKandidatInteractor: TanyaKandidatInteractor,
    private val bannerInfoInteractor: BannerInfoInteractor
) : BasePresenter<TanyaKandidatView>() {
    fun getBanner() {
        view?.showLoading()
        disposables.add(bannerInfoInteractor.getBannerInfo(PantauConstants.BANNER_TANYA)
            .subscribe(
                {
                    view?.showBanner(it)
                },
                {
                    view?.dismissLoading()
                    view?.showError(it)
                    view?.showFailedGetDataAlert()
                }
            )
        )
    }

    fun getProfile() {
        view?.bindUserData(dataCache.loadUserProfile())
    }

    fun getTanyaKandidatList(
        page: Int?,
        perPage: Int?
    ) {
        if (page == 1) {
            view?.showLoading()
        }
        disposables.add(
            tanyaKandidatInteractor
                .getTanyaKandidatlist(
                    page,
                    perPage
            )
            .subscribe(
                {
                    if (page == 1) {
                        view?.dismissLoading()
                        if (it.questions.size != 0) {
                            view?.bindDataTanyaKandidat(it.questions)
                        } else {
                            view?.showEmptyDataAlert()
                        }
                    } else {
                        if (it.questions.size != 0) {
                            view?.bindNextDataTanyaKandidat(it.questions)
                        } else {
                            view?.showEmptyNextDataAlert()
                        }
                    }
                },
                {
                    view?.dismissLoading()
                    view?.showFailedGetDataAlert()
                    view?.showError(it)
                }
            ))
    }

    fun upVoteQuestion(id: String?, questionCalass: String, isLiked: Boolean, position: Int) {
        disposables.add(
            tanyaKandidatInteractor
                .upVoteQuestion(
                    id,
                    questionCalass
                )
                .subscribe(
                    {
                        view?.onItemUpVoted(position)
                    },
                    {
                        view?.showError(it)
                        view?.onFailedUpVoteItem(isLiked, position)
                    }
                )
        )
    }

    fun reportQuestion(id: String?, className: String?) {
        disposables.add(
            tanyaKandidatInteractor
                .reportQuestion(
                    id,
                    className
                )
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
                        view?.onItemUpVoted(position)
                    },
                    {
                        view?.showError(it)
                        view?.onFailedUpVoteItem(liked, position)
                    }
                )
        )
    }
}