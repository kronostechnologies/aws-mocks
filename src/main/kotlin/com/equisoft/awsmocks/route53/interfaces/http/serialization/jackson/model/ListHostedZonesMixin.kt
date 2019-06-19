package com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model

import com.amazonaws.services.route53.model.HostedZone
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

interface ListHostedZonesMixin {
    @JacksonXmlElementWrapper(localName = "HostedZones")
    @JacksonXmlProperty(localName = "HostedZone")
    fun getHostedZones(): List<HostedZone>
}
