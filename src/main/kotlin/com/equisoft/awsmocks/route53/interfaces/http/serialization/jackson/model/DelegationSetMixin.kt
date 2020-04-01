package com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

interface DelegationSetMixin {
    @JacksonXmlElementWrapper(localName = "NameServers")
    @JacksonXmlProperty(localName = "NameServer")
    fun getNameServers(): List<String>
}
