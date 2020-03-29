package com.equisoft.awsmocks.autoscaling.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.autoscaling.model.*
import com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model.DescribeAutoScalingGroupsResponse
import com.equisoft.awsmocks.autoscaling.interfaces.http.serialization.jackson.model.DescribeLaunchConfigurationsResponse

@SuppressWarnings("LongMethod")
class AutoScalingRequestHandler(
    private val autoScalingGroupService: AutoScalingGroupService,
    private val launchConfigurationService: LaunchConfigurationService
) {
    fun handle(request: AmazonWebServiceRequest): AmazonWebServiceResult<ResponseMetadata> =
        when (request) {
            is AttachLoadBalancerTargetGroupsRequest -> {
                autoScalingGroupService.attachTargetGroup(request.autoScalingGroupName, request.targetGroupARNs)

                AttachLoadBalancerTargetGroupsResult()
            }
            is CreateAutoScalingGroupRequest -> {
                val autoScalingGroup: AutoScalingGroup = createAutoScalingGroupFromRequest(request)
                autoScalingGroupService.add(autoScalingGroup)

                CreateAutoScalingGroupResult()
            }
            is CreateLaunchConfigurationRequest -> {
                val launchConfiguration: LaunchConfiguration = createLaunchConfigurationFromRequest(request)
                launchConfigurationService.add(launchConfiguration)

                CreateLaunchConfigurationResult()
            }
            is DescribeAutoScalingGroupsRequest -> {
                val autoScalingGroups: List<AutoScalingGroup> =
                    autoScalingGroupService.getAll(request.autoScalingGroupNames)

                DescribeAutoScalingGroupsResponse(
                    DescribeAutoScalingGroupsResult().withAutoScalingGroups(autoScalingGroups)
                )
            }
            is DeleteLaunchConfigurationRequest -> {
                launchConfigurationService.remove(request.launchConfigurationName)

                DeleteLaunchConfigurationResult()
            }
            is DescribeLaunchConfigurationsRequest -> {
                val launchConfigurations: List<LaunchConfiguration> =
                    launchConfigurationService.getAll(request.launchConfigurationNames)

                DescribeLaunchConfigurationsResponse(
                    DescribeLaunchConfigurationsResult().withLaunchConfigurations(launchConfigurations)
                )
            }
            else -> throw IllegalArgumentException(request::class.qualifiedName)
        }
}
