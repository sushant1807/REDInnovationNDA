package com.redinnovationlabs.redinnovationnda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.redinnovationlabs.redinnovationnda.domain.model.NdaFormLink
import com.redinnovationlabs.redinnovationnda.domain.usecase.GetNdaFormLinkUseCase
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.model.QrCodeUiState

class QrCodeViewModel(
    getNdaFormLinkUseCase: GetNdaFormLinkUseCase
) : ViewModel() {
    private val formLink: NdaFormLink = getNdaFormLinkUseCase()

    val uiState: QrCodeUiState = QrCodeUiState(
        formLink = formLink
    )
}
