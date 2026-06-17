package com.redinnovationlabs.redinnovationnda.presentation.viewmodel.model

import com.redinnovationlabs.redinnovationnda.domain.model.NdaFormLink

data class QrCodeUiState(
    val formLink: NdaFormLink,
    val title: String = "SCAN HERE",
    val subtitle: String = "Scan the QR code and complete the NDA on your phone.",
    val shareButtonText: String = "SHARE LINK",
    val footerPrefix: String = "Need help?",
    val footerAction: String = "Contact staff"
)
