package com.redinnovationlabs.redinnovationnda.domain.usecase

import com.redinnovationlabs.redinnovationnda.domain.model.NdaFormLink
import com.redinnovationlabs.redinnovationnda.domain.repository.NdaRepository

class GetNdaFormLinkUseCase(
    private val repository: NdaRepository
) {
    operator fun invoke(): NdaFormLink = repository.getNdaFormLink()
}
