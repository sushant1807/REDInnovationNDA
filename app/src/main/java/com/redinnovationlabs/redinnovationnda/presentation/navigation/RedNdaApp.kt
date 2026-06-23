package com.redinnovationlabs.redinnovationnda.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.redinnovationlabs.redinnovationnda.data.di.RedNdaContainer
import com.redinnovationlabs.redinnovationnda.domain.model.ConnectivityStatus
import com.redinnovationlabs.redinnovationnda.presentation.components.NoInternetDialog
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.RedNdaViewModelFactory

@Composable
fun RedNdaApp(container: RedNdaContainer) {
    val navController = rememberNavController()
    val factory = remember(container) { RedNdaViewModelFactory(container) }
    val connectivityStatus by container.connectivityObserver
        .observe()
        .collectAsStateWithLifecycle(initialValue = ConnectivityStatus.Available)

    Box {
        RedNdaNavHost(
            navController = navController,
            viewModelFactory = factory
        )

        if (connectivityStatus == ConnectivityStatus.Unavailable) {
            NoInternetDialog()
        }
    }
}
