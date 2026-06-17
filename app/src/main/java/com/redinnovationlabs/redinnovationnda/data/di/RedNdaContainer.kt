package com.redinnovationlabs.redinnovationnda.data.di

import com.redinnovationlabs.redinnovationnda.data.repository.NdaRepositoryImpl
import com.redinnovationlabs.redinnovationnda.domain.repository.NdaRepository
import com.redinnovationlabs.redinnovationnda.domain.usecase.GetNdaFormLinkUseCase

class RedNdaContainer {
    private val ndaRepository: NdaRepository by lazy { NdaRepositoryImpl() }

    val getNdaFormLinkUseCase: GetNdaFormLinkUseCase by lazy {
        GetNdaFormLinkUseCase(ndaRepository)
    }
}
