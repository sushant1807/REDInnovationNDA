package com.redinnovationlabs.redinnovationnda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.redinnovationlabs.redinnovationnda.domain.model.NdaFormLink
import com.redinnovationlabs.redinnovationnda.domain.usecase.GetNdaFormLinkUseCase
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.model.WebViewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WebViewViewModel(
    getNdaFormLinkUseCase: GetNdaFormLinkUseCase
) : ViewModel() {
    private val formLink: NdaFormLink = getNdaFormLinkUseCase()

    private val _uiState = MutableStateFlow(
        WebViewUiState(
            formLink = formLink,
            isLoading = true,
            loadingProgress = 0,
            errorMessage = null
        )
    )

    val uiState: StateFlow<WebViewUiState> = _uiState.asStateFlow()

    fun markLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(
            isLoading = isLoading,
            loadingProgress = if (isLoading) _uiState.value.loadingProgress else 100
        )
    }

    fun updateProgress(progress: Int) {
        _uiState.value = _uiState.value.copy(
            isLoading = progress < 100,
            loadingProgress = progress.coerceIn(0, 100)
        )
    }

    fun setError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            loadingProgress = 0,
            errorMessage = message
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            loadingProgress = 0,
            errorMessage = null
        )
    }
}
