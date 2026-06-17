package com.redinnovationlabs.redinnovationnda.presentation.viewmodel.model

import com.redinnovationlabs.redinnovationnda.domain.model.NdaFormLink

data class WebViewUiState(
    val formLink: NdaFormLink,
    val isLoading: Boolean = true,
    val loadingProgress: Int = 0,
    val errorMessage: String? = null,
    val screenTitle: String = "Complete NDA",
    val retryButtonText: String = "RETRY",
    val backButtonText: String = "GO BACK"
)
