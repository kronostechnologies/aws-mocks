package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.ec2.model.GroupIdentifier
import com.amazonaws.services.ec2.model.InstanceAttribute
import com.amazonaws.services.ec2.model.InstanceBlockDeviceMapping
import com.equisoft.awsmocks.common.interfaces.http.serialization.jackson.ListItem
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeInstanceAttributeResponse")
class DescribeInstanceAttributeResponse(instanceAttribute: InstanceAttribute) :
    AmazonWebServiceResult<ResponseMetadata>() {

    val blockDeviceMappings: List<InstanceBlockDeviceMapping>? = instanceAttribute.blockDeviceMappings
    val disableApiTermination = instanceAttribute.disableApiTermination?.let(::AttributeBooleanValue)
    val ebsOptimized = instanceAttribute.ebsOptimized?.let(::AttributeBooleanValue)
    val enaSupport = instanceAttribute.enaSupport?.let(::AttributeBooleanValue)
    val instanceId: String = instanceAttribute.instanceId
    val instanceType = instanceAttribute.instanceType?.let(::AttributeValue)
    val kernel = instanceAttribute.kernelId?.let(::AttributeValue)
    val ramdisk = instanceAttribute.ramdiskId?.let(::AttributeValue)
    val rootDeviceName = instanceAttribute.rootDeviceName?.let(::AttributeValue)
    val sriovNetSupport = instanceAttribute.sriovNetSupport?.let(::AttributeValue)

    @ListItem("groupSet")
    val groups: List<GroupIdentifier> = instanceAttribute.groups
}
