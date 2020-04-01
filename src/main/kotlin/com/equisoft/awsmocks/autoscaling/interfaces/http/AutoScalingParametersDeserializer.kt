package com.equisoft.awsmocks.autoscaling.interfaces.http

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.autoscaling.model.*
import com.equisoft.awsmocks.common.interfaces.http.ParametersDeserializer
import com.equisoft.awsmocks.common.interfaces.http.toList
import com.equisoft.awsmocks.common.interfaces.http.toObjects
import io.ktor.http.Parameters

@SuppressWarnings("LongMethod")
class AutoScalingParametersDeserializer : ParametersDeserializer {
    @OptIn(ExperimentalStdlibApi::class)
    override fun addParameters(parameters: Parameters, request: AmazonWebServiceRequest): AmazonWebServiceRequest =
        when (request) {
            is AttachLoadBalancerTargetGroupsRequest -> {
                request.withAutoScalingGroupName(parameters["AutoScalingGroupName"])
                    .withTargetGroupARNs(parameters.toList("TargetGroupARNs.member"))
            }
            is CreateAutoScalingGroupRequest -> {
                request.withAutoScalingGroupName(parameters["AutoScalingGroupName"])
                    .withAvailabilityZones(parameters.toList("AvailabilityZones.member"))
                    .withDefaultCooldown(parameters["DefaultCooldown"]?.toInt())
                    .withDesiredCapacity(parameters["DesiredCapacity"]?.toInt())
                    .withHealthCheckGracePeriod(parameters["HealthCheckGracePeriod"]?.toInt())
                    .withHealthCheckType(parameters["HealthCheckType"])
                    .withInstanceId(parameters["InstanceId"])
                    .withLaunchConfigurationName(parameters["LaunchConfigurationName"])
                    .withLaunchTemplate(parseLaunchTemplateSpecification(parameters))
                    .withLifecycleHookSpecificationList(parseLifecycleHookSpecifications(parameters))
                    .withLoadBalancerNames(parameters.toList("LoadBalancerNames.member"))
                    .withMaxInstanceLifetime(parameters["MaxInstanceLifetime"]?.toInt())
                    .withMaxSize(parameters["MaxSize"]?.toInt())
                    .withMinSize(parameters["MinSize"]?.toInt())
                    .withNewInstancesProtectedFromScaleIn(parameters["NewInstancesProtectedFromScaleIn"]?.toBoolean())
                    .withPlacementGroup(parameters["PlacementGroup"])
                    .withServiceLinkedRoleARN(parameters["ServiceLinkedRoleARN"])
                    .withTags(parameters.getTags())
                    .withTargetGroupARNs(parameters.toList("TargetGroupARNs.member"))
                    .withTerminationPolicies(parameters.toList("TerminationPolicies.member"))
                    .withVPCZoneIdentifier(parameters["VPCZoneIdentifier"])
            }
            is CreateLaunchConfigurationRequest -> {
                request.withAssociatePublicIpAddress(parameters["AssociatePublicIpAddress"]?.toBoolean())
                    .withClassicLinkVPCId(parameters["ClassicLinkVPCId"])
                    .withClassicLinkVPCSecurityGroups(parameters.toList("ClassicLinkVPCSecurityGroups.member"))
                    .withEbsOptimized(parameters["EbsOptimized"]?.toBoolean())
                    .withIamInstanceProfile(parameters["IamInstanceProfile"])
                    .withImageId(parameters["ImageId"])
                    .withInstanceId(parameters["InstanceId"])
                    .withInstanceMonitoring(
                        InstanceMonitoring().withEnabled(parameters["InstanceMonitoring"]?.toBoolean() ?: true))
                    .withInstanceType(parameters["InstanceType"])
                    .withKernelId(parameters["KernelId"])
                    .withKeyName(parameters["KeyName"])
                    .withLaunchConfigurationName(parameters["LaunchConfigurationName"])
                    .withPlacementTenancy(parameters["PlacementTenancy"])
                    .withRamdiskId(parameters["RamdiskId"])
                    .withSecurityGroups(parameters.toList("SecurityGroups.member"))
                    .withSpotPrice(parameters["SpotPrice"])
                    .withUserData(parameters["UserData"])
            }
            is DeleteLaunchConfigurationRequest -> {
                request.withLaunchConfigurationName(parameters["LaunchConfigurationName"])
            }
            is DescribeAutoScalingGroupsRequest -> {
                request.withAutoScalingGroupNames(parameters.toList("AutoScalingGroupNames.member"))
            }
            is DescribeLaunchConfigurationsRequest -> {
                request.withLaunchConfigurationNames(parameters.toList("LaunchConfigurationNames.member"))
            }
            else -> request
        }

    private fun parseLifecycleHookSpecifications(parameters: Parameters): List<LifecycleHookSpecification> =
        parameters.toObjects("LifecycleHookSpecificationList.member",
            { LifecycleHookSpecification() }) { _, hook, (key, value) ->
            when (key) {
                "DefaultResult" -> hook.withDefaultResult(value)
                "HeartbeatTimeout" -> hook.withHeartbeatTimeout(value.toInt())
                "LifecycleHookName" -> hook.withLifecycleHookName(value)
                "LifecycleTransition" -> hook.withLifecycleTransition(value)
                "NotificationMetadata" -> hook.withNotificationMetadata(value)
                "NotificationTargetARN" -> hook.withNotificationTargetARN(value)
                "RoleARN" -> hook.withRoleARN(value)
                else -> hook
            }
        }

    private fun parseLaunchTemplateSpecification(parameters: Parameters): LaunchTemplateSpecification? {
        val launchTemplateId: String? = parameters["LaunchTemplate.LaunchTemplateId"]
        val launchTemplateName: String? = parameters["LaunchTemplate.LaunchTemplateName"]

        if (launchTemplateId == null && launchTemplateName == null) {
            return null
        }

        return LaunchTemplateSpecification().withLaunchTemplateId(launchTemplateId)
            .withLaunchTemplateName(launchTemplateName)
            .withVersion(parameters["LaunchTemplate.Version"])
    }
}

private fun Parameters.getTags(name: String = "Tags.member"): List<Tag> = toObjects(name, { Tag() },
    { _, tag, (key, value) ->
        when (key) {
            "Key" -> tag.withKey(value)
            "PropagateAtLaunch" -> tag.withPropagateAtLaunch(value.toBoolean())
            "ResourceId" -> tag.withResourceId(value)
            "ResourceType" -> tag.withResourceType(value)
            "Value" -> tag.withValue(value)
            else -> tag
        }
    })
