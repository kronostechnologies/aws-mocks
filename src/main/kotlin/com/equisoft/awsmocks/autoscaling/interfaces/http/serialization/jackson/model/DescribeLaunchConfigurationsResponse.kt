package com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model

import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.autoscaling.model.DescribeLaunchConfigurationsResult
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeLaunchConfigurationsResponse")
class DescribeLaunchConfigurationsResponse(
    val describeLaunchConfigurationsResult: DescribeLaunchConfigurationsResult
) : AmazonWebServiceResult<ResponseMetadata>()
