package com.pantaubersama.app.ui.login

import com.pantaubersama.app.base.BaseView
import com.pantaubersama.app.data.interactors.LoginInteractor
import com.pantaubersama.app.data.model.accesstoken.Token
import io.reactivex.Single
import org.junit.After
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
    private val view: LoginView? = null

    @Mock
    private val loginInteractor: LoginInteractor? = null

    @Mock
    private val token: Token? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = spy(LoginPresenter(loginInteractor))
        presenter?.attach(view as BaseView)
    }

    @Test
    fun exchangeToken() {
        val oAuthToken = ""
        val registrationId = ""
        presenter?.exchangeToken(oAuthToken, registrationId)
        verify(view, times(1))?.showLoading()
        verify(loginInteractor, times(1))?.exchangeToken(oAuthToken, firebaseToken)
    }

    @Test
    fun loginSuccess() {
        val oAuthToken = "token"
        val registrationId = ""
        doReturn(Single.just(token)).`when`(loginInteractor)?.exchangeToken(oAuthToken, firebaseToken)
        presenter?.exchangeToken(oAuthToken, registrationId)
        verify(view, times(1))?.openHomeActivity()
        verify(view, times(1))?.dismissLoading()
    }

//    @Test
//    fun loginFailure() {
//        val oAuthToken = "token"
//        val registrationId = ""
//        doReturn(Single.error(Throwable())).`when`(loginInteractor)?.exchangeToken(oAuthToken)
//        presenter?.exchangeToken(oAuthToken, registrationId)
//        verify(view, times(1))?.showLoginFailedAlert()
//        verify(view, times(1))?.dismissLoading()
//        verify(view, times(1))?.showError(Throwable())
//    }

    @After
    fun tearDown() {
        presenter?.detach()
    }
}