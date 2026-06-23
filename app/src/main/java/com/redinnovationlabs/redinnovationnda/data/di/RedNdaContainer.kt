package com.redinnovationlabs.redinnovationnda.data.di

import android.content.Context
import com.redinnovationlabs.redinnovationnda.data.repository.AndroidConnectivityObserver
import com.redinnovationlabs.redinnovationnda.data.repository.NdaRepositoryImpl
import com.redinnovationlabs.redinnovationnda.domain.repository.ConnectivityObserver
import com.redinnovationlabs.redinnovationnda.domain.repository.NdaRepository
import com.redinnovationlabs.redinnovationnda.domain.usecase.GetNdaFormLinkUseCase

class RedNdaContainer(
    context: Context
) {
    private val appContext = context.applicationContext
    private val ndaRepository: NdaRepository by lazy { NdaRepositoryImpl(appContext) }
    val connectivityObserver: ConnectivityObserver by lazy {
        AndroidConnectivityObserver(appContext)
    }

    val getNdaFormLinkUseCase: GetNdaFormLinkUseCase by lazy {
        GetNdaFormLinkUseCase(ndaRepository)
    }
}
