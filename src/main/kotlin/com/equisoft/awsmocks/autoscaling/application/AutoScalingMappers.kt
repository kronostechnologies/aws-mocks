package com.equisoft.awsmocks.autoscaling.application

import com.amazonaws.services.autoscaling.model.AutoScalingGroup
import com.amazonaws.services.autoscaling.model.CreateAutoScalingGroupRequest
import com.amazonaws.services.autoscaling.model.CreateLaunchConfigurationRequest
import com.amazonaws.services.autoscaling.model.Instance
import com.amazonaws.services.autoscaling.model.LaunchConfiguration
import com.amazonaws.services.autoscaling.model.LifecycleState
import com.amazonaws.services.autoscaling.model.NotificationConfiguration
import com.amazonaws.services.autoscaling.model.PutNotificationConfigurationRequest
import com.amazonaws.services.autoscaling.model.TagDescription
import com.equisoft.awsmocks.common.infrastructure.aws.AwsService.AUTOSCALING
import com.equisoft.awsmocks.common.interfaces.http.accountId
import com.equisoft.awsmocks.common.interfaces.http.region
import com.equisoft.awsmocks.common.utils.mapEach
import java.util.Date
import java.util.UUID

fun createLaunchConfigurationFromRequest(request: CreateLaunchConfigurationRequest): LaunchConfiguration =
    LaunchConfiguration().withAssociatePublicIpAddress(request.associatePublicIpAddress)
        .withBlockDeviceMappings(request.blockDeviceMappings)
        .withClassicLinkVPCId(request.classicLinkVPCId)
        .withClassicLinkVPCSecurityGroups(request.classicLinkVPCSecurityGroups)
        .withCreatedTime(Date())
        .withEbsOptimized(request.ebsOptimized)
        .withIamInstanceProfile(request.iamInstanceProfile)
        .withImageId(request.imageId)
        .withInstanceMonitoring(request.instanceMonitoring)
        .withInstanceType(request.instanceType)
        .withKernelId(request.kernelId)
        .withKeyName(request.keyName)
        .withLaunchConfigurationName(request.launchConfigurationName)
        .withLaunchConfigurationARN(AUTOSCALING.createArn(request.accountId,
            "launchConfiguration:${UUID.randomUUID()}:launchConfigurationName/${request.launchConfigurationName}",
            request.region))
        .withPlacementTenancy(request.placementTenancy)
        .withRamdiskId(request.ramdiskId)
        .withSecurityGroups(request.securityGroups)
        .withSpotPrice(request.spotPrice)
        .withUserData(request.userData)

fun createAutoScalingGroupFromRequest(request: CreateAutoScalingGroupRequest): AutoScalingGroup = AutoScalingGroup()
    .withAutoScalingGroupARN(AUTOSCALING.createArn(request.accountId,
        "autoScalingGroup:${UUID.randomUUID()}:autoScalingGroupName/${request.autoScalingGroupName}"))
    .withAutoScalingGroupName(request.autoScalingGroupName)
    .withNewInstancesProtectedFromScaleIn(request.isNewInstancesProtectedFromScaleIn)
    .withPlacementGroup(request.placementGroup)
    .withTargetGroupARNs(request.targetGroupARNs)
    .withAvailabilityZones(request.availabilityZones)
    .withCreatedTime(Date())
    .withDefaultCooldown(request.defaultCooldown)
    .withDesiredCapacity(request.desiredCapacity)
    .withHealthCheckGracePeriod(request.healthCheckGracePeriod)
    .withHealthCheckType(request.healthCheckType)
    .withInstances(request.desiredCapacity?.mapEach {
        Instance().withAvailabilityZone(request.availabilityZones.firstOrNull())
            .withHealthStatus("Healthy")
            .withInstanceId(UUID.randomUUID().toString())
            .withLaunchConfigurationName(request.launchConfigurationName)
            .withLaunchTemplate(request.launchTemplate)
            .withLifecycleState(LifecycleState.InService)
    })
    .withLaunchConfigurationName(request.launchConfigurationName)
    .withLaunchTemplate(request.launchTemplate)
    .withLoadBalancerNames(request.loadBalancerNames)
    .withMaxInstanceLifetime(request.maxInstanceLifetime)
    .withMaxSize(request.maxSize)
    .withMinSize(request.minSize)
    .withMixedInstancesPolicy(request.mixedInstancesPolicy)
    .withServiceLinkedRoleARN(request.serviceLinkedRoleARN)
    .withTags(request.tags.map {
        TagDescription().withKey(it.key)
            .withPropagateAtLaunch(it.propagateAtLaunch)
            .withResourceId(it.resourceId)
            .withResourceType(it.resourceType)
            .withValue(it.value)
    })
    .withTerminationPolicies(request.terminationPolicies)
    .withVPCZoneIdentifier(request.vpcZoneIdentifier?.trim(','))

fun createNotificationConfigurationsFromRequest(
    request: PutNotificationConfigurationRequest
): List<NotificationConfiguration> = request.notificationTypes.map {
    NotificationConfiguration().withAutoScalingGroupName(request.autoScalingGroupName)
        .withNotificationType(it)
        .withTopicARN(request.topicARN)
}
