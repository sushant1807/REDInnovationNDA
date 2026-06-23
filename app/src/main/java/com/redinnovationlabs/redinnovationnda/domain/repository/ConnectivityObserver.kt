package com.redinnovationlabs.redinnovationnda.domain.repository

import com.redinnovationlabs.redinnovationnda.domain.model.ConnectivityStatus
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<ConnectivityStatus>
}
