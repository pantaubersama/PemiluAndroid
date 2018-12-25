package com.pantaubersama.app.ui.penpol.tanyakandidat.create

import com.pantaubersama.app.base.BaseView
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.* // ktlint-disable
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateTanyaKandidatPresenterTest {
    private var presenter: CreateTanyaKandidatPresenter? = null

    @Mock
    private var view: CreateTanyaKandidatView? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = spy(CreateTanyaKandidatPresenter())
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
        // continue to data manager
    }

    @After
    fun tearDown() {
        presenter?.detach()
    }
}