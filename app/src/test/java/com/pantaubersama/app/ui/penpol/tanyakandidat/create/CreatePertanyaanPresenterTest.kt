package com.pantaubersama.app.ui.penpol.tanyakandidat.create

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.interactors.TanyaKandidatInteractor
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.* // ktlint-disable
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreatePertanyaanPresenterTest {
    private var presenter: CreateTanyaKandidatPresenter? = null

    @Mock
    private var interactor: TanyaKandidatInteractor? = null

    @Mock
    private var view: CreateTanyaKandidatView? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = spy(CreateTanyaKandidatPresenter(interactor))
        presenter?.attach(view as BaseView)
    }

    @Test
    fun submitQuestionShouldShowEmptyAlert() {
        val question: String?
        question = ""
        presenter?.submitQuestion(question)
        verify(view, times(1))?.showEmptyQuestionAlert()
    }

    @Test
    fun submitQuestionShouldContinue() {
        val question: String?
        question = "Some question"
        presenter?.submitQuestion(question)
        // continue to pilpresList manager
    }

    @After
    fun tearDown() {
        presenter?.detach()
    }
}