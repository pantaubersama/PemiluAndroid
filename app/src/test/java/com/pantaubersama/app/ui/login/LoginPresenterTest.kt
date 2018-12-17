package com.pantaubersama.app.ui.login

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.utils.RxSchedulers
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.* // ktlint-disable
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginPresenterTest {
    private var presenter: LoginPresenter? = null

    @Mock
    private var view: LoginView? = null

    @Mock
    private var rxSchedulers: RxSchedulers? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = spy(LoginPresenter(rxSchedulers))
        presenter?.attach(view as BaseView)
    }

    @Test
    fun exchangeToken() {
        val oAuthToken = ""
        val registrationId = ""
        presenter?.exchangeToken(oAuthToken, registrationId)
        verify(view, times(1))?.showLoading()
    }
}