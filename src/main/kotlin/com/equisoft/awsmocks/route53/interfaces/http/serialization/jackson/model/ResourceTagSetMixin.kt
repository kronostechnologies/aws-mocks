package com.equisoft.awsmocks.route53.interfaces.http.serialization.jackson.model

import com.amazonaws.services.route53.model.Tag
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

interface ResourceTagSetMixin {
    @JacksonXmlElementWrapper(localName = "Tags")
    @JacksonXmlProperty(localName = "Tag")
    fun getTags(): List<Tag>
}
