package com.equisoft.awsmocks.ec2.context

import com.amazonaws.services.ec2.model.DescribeVpcEndpointServicesResult
import com.equisoft.awsmocks.ec2.infrastructure.persistence.ServiceDetailRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class Ec2Bootstrapper(
    serviceDetailRepository: ServiceDetailRepository,
    objectMapper: ObjectMapper
) {
    init {
        // This comes from "aws --region us-east-1 ec2 describe-vpc-endpoint-services"
        val endpointsResult: DescribeVpcEndpointServicesResult =
            objectMapper.readValue(javaClass.classLoader.getResourceAsStream("vpc-endpoint-services.json"))

        endpointsResult.serviceDetails.forEach { serviceDetailRepository[it.serviceName] = it }
    }
}
