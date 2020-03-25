package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.ec2.model.InstanceStateChange
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "TerminateInstancesResponse")
class TerminateInstancesResponse(
    @get:ListItem("instancesSet") val instances: List<InstanceStateChange>
) : AmazonWebServiceResult<ResponseMetadata>()
