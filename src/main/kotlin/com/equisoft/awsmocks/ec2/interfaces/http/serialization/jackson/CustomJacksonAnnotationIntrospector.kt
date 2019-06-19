package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson

import com.fasterxml.jackson.databind.PropertyName
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector

class CustomJacksonAnnotationIntrospector : JacksonAnnotationIntrospector() {
    override fun findNameForSerialization(annotated: Annotated): PropertyName? {
        val listItem: ListItem? = annotated.getAnnotation(ListItem::class.java)

        return if (listItem != null) {
            PropertyName.construct(listItem.value)
        } else {
            super.findNameForSerialization(annotated)
        }
    }
}
