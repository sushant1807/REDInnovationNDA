package com.redinnovationlabs.redinnovationnda.data.repository

import android.content.Context
import com.redinnovationlabs.redinnovationnda.R
import com.redinnovationlabs.redinnovationnda.data.constants.DocuSignConstants
import com.redinnovationlabs.redinnovationnda.domain.model.NdaFormLink
import com.redinnovationlabs.redinnovationnda.domain.repository.NdaRepository

class NdaRepositoryImpl(
    private val context: Context
) : NdaRepository {
    override fun getNdaFormLink(): NdaFormLink {
        return NdaFormLink(
            url = DocuSignConstants.NDA_WEBFORM_URL,
            title = context.getString(R.string.nda_webform_title),
            shareSubject = context.getString(R.string.nda_share_subject),
            shareMessagePrefix = context.getString(R.string.nda_share_message_prefix)
        )
    }
}
