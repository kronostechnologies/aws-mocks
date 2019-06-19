package com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model

import com.amazonaws.services.route53.model.ResourceRecordSet
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

interface ListResourceRecordSetsMixin {
    @JacksonXmlElementWrapper(localName = "ResourceRecordSets")
    @JacksonXmlProperty(localName = "ResourceRecordSet")
    fun getResourceRecordSets(): List<ResourceRecordSet>
}
