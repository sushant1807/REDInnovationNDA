package com.redinnovationlabs.redinnovationnda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.redinnovationlabs.redinnovationnda.data.di.RedNdaContainer

class RedNdaViewModelFactory(
    private val container: RedNdaContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel()
            modelClass.isAssignableFrom(WebViewViewModel::class.java) -> WebViewViewModel(
                getNdaFormLinkUseCase = container.getNdaFormLinkUseCase
            )
            modelClass.isAssignableFrom(QrCodeViewModel::class.java) -> QrCodeViewModel(
                getNdaFormLinkUseCase = container.getNdaFormLinkUseCase
            )
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }
}
