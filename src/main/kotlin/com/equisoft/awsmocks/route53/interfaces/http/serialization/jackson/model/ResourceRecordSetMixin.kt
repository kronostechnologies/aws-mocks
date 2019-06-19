package com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model

import com.amazonaws.services.route53.model.ResourceRecord
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

interface ResourceRecordSetMixin {
    @JacksonXmlElementWrapper(localName = "ResourceRecords")
    @JacksonXmlProperty(localName = "ResourceRecord")
    fun getResourceRecords(): List<ResourceRecord>
}
