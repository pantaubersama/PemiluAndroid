package com.pantaubersama.app.di.component

import com.pantaubersama.app.background.downloadc1.DownloadC1Service
import com.pantaubersama.app.background.firebase.PantauFirebaseMessagingService
import com.pantaubersama.app.background.uploadtps.UploadTpsService
import com.pantaubersama.app.di.module.ServiceModule
import com.pantaubersama.app.di.scope.ServiceScope
import dagger.Subcomponent

/**
 * Created by ali on 19/10/17.
 */
@ServiceScope
@Subcomponent(modules = [ServiceModule::class])
interface ServiceComponent {
    fun inject(pantauFirebaseMessagingService: PantauFirebaseMessagingService)
    fun inject(uploadTPSService: UploadTpsService)
    fun inject(downloadC1Service: DownloadC1Service)
}