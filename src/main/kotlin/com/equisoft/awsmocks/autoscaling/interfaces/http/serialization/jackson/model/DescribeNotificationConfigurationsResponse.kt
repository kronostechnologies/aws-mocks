package com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.autoscaling.model.DescribeNotificationConfigurationsResult
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeNotificationConfigurationsResponse")
class DescribeNotificationConfigurationsResponse(
    val describeNotificationConfigurationsResult: DescribeNotificationConfigurationsResult
) : AmazonWebServiceResult<ResponseMetadata>()
