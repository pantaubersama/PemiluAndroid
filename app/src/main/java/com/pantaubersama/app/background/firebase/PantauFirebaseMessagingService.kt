package com.pantaubersama.app.background.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.di.module.ServiceModule
import javax.inject.Inject

class PantauFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var presenter: PantauFirebaseMessagingPresenter

    override fun onCreate() {
        super.onCreate()
        (application as BaseApp).appComponent.withServiceComponent(ServiceModule(this)).inject(this)
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        presenter.saveFirebaseNewToken(token)
    }
}