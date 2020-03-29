package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson

import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import com.fasterxml.jackson.databind.PropertyName
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector

class Ec2JacksonAnnotationIntrospector : JacksonAnnotationIntrospector() {
    override fun findNameForSerialization(annotated: Annotated): PropertyName? {
        val listItem: ListItem? = annotated.getAnnotation(ListItem::class.java)
        val wrapError: WrapError? = annotated.getAnnotation(WrapError::class.java)

        return when {
            listItem != null -> PropertyName.construct(listItem.value)
            wrapError != null -> PropertyName.construct(wrapError.value)
            else -> super.findNameForSerialization(annotated)
        }
    }
}
