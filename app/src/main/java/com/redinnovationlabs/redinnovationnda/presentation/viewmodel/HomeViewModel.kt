package com.redinnovationlabs.redinnovationnda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.redinnovationlabs.redinnovationnda.presentation.viewmodel.model.HomeUiState

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun showHelpDialog() {
        _uiState.value = _uiState.value.copy(showHelpDialog = true)
    }

    fun dismissHelpDialog() {
        _uiState.value = _uiState.value.copy(showHelpDialog = false)
    }
}
