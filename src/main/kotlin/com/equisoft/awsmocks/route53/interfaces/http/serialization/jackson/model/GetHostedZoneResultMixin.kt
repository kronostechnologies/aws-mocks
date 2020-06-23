package com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model

import com.amazonaws.services.route53.model.VPC
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

interface GetHostedZoneResultMixin {
    @JacksonXmlElementWrapper(localName = "VPCs")
    @JacksonXmlProperty(localName = "VPC")
    fun getVPCs(): List<VPC>
}
