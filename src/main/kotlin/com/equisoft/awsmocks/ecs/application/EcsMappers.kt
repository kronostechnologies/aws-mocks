@file:SuppressWarnings("LongMethod")

package com.equisoft.awsmocks.ecs.application

import com.amazonaws.services.ecs.model.Cluster
import com.amazonaws.services.ecs.model.CreateClusterRequest
import com.amazonaws.services.ecs.model.CreateServiceRequest
import com.amazonaws.services.ecs.model.RegisterTaskDefinitionRequest
import com.amazonaws.services.ecs.model.Service
import com.amazonaws.services.ecs.model.TaskDefinition
import com.amazonaws.services.ecs.model.TaskDefinitionStatus
import com.equisoft.awsmocks.common.infrastructure.aws.AwsService.ECS
import com.equisoft.awsmocks.common.interfaces.http.accountId
import com.equisoft.awsmocks.common.interfaces.http.region
import java.util.Date

fun createClusterFromRequest(request: CreateClusterRequest): Cluster = Cluster()
    .withClusterName(request.clusterName)
    .withClusterArn(ECS.createArn(request.accountId, "cluster/${request.clusterName}", request.region))
    .withCapacityProviders(request.capacityProviders)
    .withDefaultCapacityProviderStrategy(request.defaultCapacityProviderStrategy)
    .withSettings(request.settings)
    .withStatus("ACTIVE")
    .withTags(request.tags)

fun createTaskDefinitionFromRequest(request: RegisterTaskDefinitionRequest): TaskDefinition = TaskDefinition()
    .withCompatibilities(request.requiresCompatibilities)
    .withContainerDefinitions(request.containerDefinitions)
    .withCpu(request.cpu)
    .withExecutionRoleArn(request.executionRoleArn)
    .withTaskRoleArn(request.taskRoleArn)
    .withFamily(request.family)
    .withInferenceAccelerators(request.inferenceAccelerators)
    .withIpcMode(request.ipcMode)
    .withMemory(request.memory)
    .withNetworkMode(request.networkMode)
    .withPidMode(request.pidMode)
    .withPlacementConstraints(request.placementConstraints)
    .withProxyConfiguration(request.proxyConfiguration)
    .withRequiresAttributes()
    .withRequiresCompatibilities(request.requiresCompatibilities)
    .withRevision(1)
    .withStatus(TaskDefinitionStatus.ACTIVE)
    .withTaskRoleArn(request.taskRoleArn)
    .withTaskDefinitionArn(ECS.createArn(request.accountId, "taskdefinition/${request.family}:1", request.region))
    .withVolumes(request.volumes)

fun createServiceFromRequest(request: CreateServiceRequest): Service = Service()
    .withServiceArn(ECS.createArn(request.accountId, "service/${request.serviceName}", request.region))
    .withServiceName(request.serviceName)
    .withServiceRegistries(request.serviceRegistries)
    .withCapacityProviderStrategy(request.capacityProviderStrategy)
    .withClusterArn(request.cluster)
    .withCreatedAt(Date())
    .withCreatedBy(request.accountId)
    .withDeploymentConfiguration(request.deploymentConfiguration)
    .withDeploymentController(request.deploymentController)
    .withDesiredCount(request.desiredCount)
    .withEnableECSManagedTags(request.enableECSManagedTags)
    .withHealthCheckGracePeriodSeconds(request.healthCheckGracePeriodSeconds)
    .withLaunchType(request.launchType)
    .withLoadBalancers(request.loadBalancers)
    .withNetworkConfiguration(request.networkConfiguration)
    .withPendingCount(0)
    .withPlacementConstraints(request.placementConstraints)
    .withPlacementStrategy(request.placementStrategy)
    .withPlatformVersion(request.platformVersion)
    .withPropagateTags(request.propagateTags)
    .withRoleArn(request.role)
    .withSchedulingStrategy(request.schedulingStrategy)
    .withStatus("ACTIVE")
    .withTags(request.tags)
    .withTaskDefinition(request.taskDefinition)