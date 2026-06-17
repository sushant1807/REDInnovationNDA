package com.redinnovationlabs.redinnovationnda.presentation.viewmodel.model

data class HomeUiState(
    val logoRed: String = "RED",
    val logoBlack: String = "INNOVATION",
    val logoRedBottom: String = "EXPERIENCE",
    val logoCaption: String = "RED AUTOMATION LABS",
    val welcomeTitle: String = "Welcome",
    val welcomeSubtitle: String = "Please choose how you want to complete the NDA.",
    val proceedLineOne: String = "PROCEED WITH",
    val proceedAccent: String = "NDA",
    val proceedLineTwo: String = "HERE",
    val proceedDescription: String = "Fill in your details, review and sign the NDA on this device.",
    val proceedButtonText: String = "START NDA",
    val scanAccent: String = "SCAN",
    val scanLineTwo: String = "HERE",
    val scanDescription: String = "Scan the QR code and complete the NDA on your phone.",
    val scanButtonText: String = "SHOW QR CODE",
    val helpText: String = "Help",
    val helpDialogTitle: String = "Need help?",
    val helpDialogMessage: String = "Please contact RED Automation Labs staff for help completing the NDA.",
    val helpDialogButton: String = "CLOSE",
    val showHelpDialog: Boolean = false
)
