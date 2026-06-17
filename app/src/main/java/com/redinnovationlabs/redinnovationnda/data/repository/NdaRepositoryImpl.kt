package com.redinnovationlabs.redinnovationnda.data.repository

import com.redinnovationlabs.redinnovationnda.data.constants.DocuSignConstants
import com.redinnovationlabs.redinnovationnda.domain.model.NdaFormLink
import com.redinnovationlabs.redinnovationnda.domain.repository.NdaRepository

class NdaRepositoryImpl : NdaRepository {
    override fun getNdaFormLink(): NdaFormLink {
        return NdaFormLink(
            url = DocuSignConstants.NDA_WEBFORM_URL,
            title = DocuSignConstants.NDA_WEBFORM_TITLE,
            shareSubject = DocuSignConstants.NDA_SHARE_SUBJECT,
            shareMessagePrefix = DocuSignConstants.NDA_SHARE_MESSAGE_PREFIX
        )
    }
}
