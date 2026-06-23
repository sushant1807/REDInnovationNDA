package com.redinnovationlabs.redinnovationnda.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.redinnovationlabs.redinnovationnda.domain.model.ConnectivityStatus
import com.redinnovationlabs.redinnovationnda.domain.repository.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class AndroidConnectivityObserver(
    context: Context
) : ConnectivityObserver {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityStatus> = callbackFlow {
        fun currentStatus(): ConnectivityStatus {
            val network = connectivityManager.activeNetwork ?: return ConnectivityStatus.Unavailable
            val capabilities = connectivityManager.getNetworkCapabilities(network)
                ?: return ConnectivityStatus.Unavailable

            val hasInternet =
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            val isValidated =
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            return if (hasInternet && isValidated) {
                ConnectivityStatus.Available
            } else {
                ConnectivityStatus.Unavailable
            }
        }

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(currentStatus())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val hasInternet = networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                )
                val isValidated = networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )

                trySend(
                    if (hasInternet && isValidated) {
                        ConnectivityStatus.Available
                    } else {
                        ConnectivityStatus.Unavailable
                    }
                )
            }

            override fun onLost(network: Network) {
                trySend(currentStatus())
            }

            override fun onUnavailable() {
                trySend(ConnectivityStatus.Unavailable)
            }
        }

        trySend(currentStatus())

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose {
            runCatching {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }
    }.distinctUntilChanged()
}
