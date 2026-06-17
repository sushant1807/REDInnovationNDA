package com.redinnovationlabs.redinnovationnda.domain.repository

import com.redinnovationlabs.redinnovationnda.domain.model.NdaFormLink

interface NdaRepository {
    fun getNdaFormLink(): NdaFormLink
}
