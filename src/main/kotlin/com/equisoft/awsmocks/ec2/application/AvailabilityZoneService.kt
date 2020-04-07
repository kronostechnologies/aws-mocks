package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult
import com.equisoft.awsmocks.common.infrastructure.aws.AwsService
import com.equisoft.awsmocks.common.infrastructure.aws.readResource

class AvailabilityZoneService {
    fun getAll(region: String): DescribeAvailabilityZonesResult =
        readResource(AwsService.EC2, "availabilityzones/$region")
}
