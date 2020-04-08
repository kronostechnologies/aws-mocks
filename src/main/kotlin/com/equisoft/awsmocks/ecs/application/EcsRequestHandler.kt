package com.equisoft.awsmocks.ecs.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.ecs.model.*
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository

@SuppressWarnings("LongMethod")
class EcsRequestHandler(
    private val capacityProviderService: CapacityProviderService,
    private val clusterService: ClusterService,
    private val serviceService: ServiceService,
    private val taskDefinitionService: TaskDefinitionService,
    private val resourceTagsRepository: ResourceTagsRepository<Tag>
) {
    fun handle(request: AmazonWebServiceRequest): AmazonWebServiceResult<ResponseMetadata> {
        return when (request) {
            is CreateCapacityProviderRequest -> {
                val capacityProvider: CapacityProvider = createCapacityProviderFromRequest(request)
                capacityProviderService.add(capacityProvider)

                CreateCapacityProviderResult().withCapacityProvider(capacityProvider)
            }
            is CreateClusterRequest -> {
                val cluster: Cluster = createClusterFromRequest(request)
                clusterService.add(cluster)

                CreateClusterResult().withCluster(cluster)
            }
            is CreateServiceRequest -> {
                val service: Service = createServiceFromRequest(request)
                serviceService.add(service)

                CreateServiceResult().withService(service)
            }
            is DescribeCapacityProvidersRequest -> {
                val capacityProviders: List<CapacityProvider> =
                    capacityProviderService.getAllByArn(request.capacityProviders)

                DescribeCapacityProvidersResult().withCapacityProviders(capacityProviders)
            }
            is DescribeClustersRequest -> {
                val searchResult: SearchResult<Cluster> = clusterService.getAll(request.clusters, request.include)

                DescribeClustersResult()
                    .withClusters(searchResult.getFound()).withFailures(searchResult.getFailures())
            }
            is DescribeServicesRequest -> {
                val searchResult: SearchResult<Service> = serviceService.getAll(request.cluster, request.services)

                DescribeServicesResult().withServices(searchResult.getFound()).withFailures(searchResult.getFailures())
            }
            is DescribeTaskDefinitionRequest -> {
                val taskDefinition: TaskDefinition = taskDefinitionService.getByArn(request.taskDefinition)
                val tags: List<Tag> = resourceTagsRepository[taskDefinition.taskDefinitionArn].orEmpty()

                DescribeTaskDefinitionResult().withTaskDefinition(taskDefinition).withTags(tags)
            }
            is RegisterTaskDefinitionRequest -> {
                val taskDefinition: TaskDefinition = createTaskDefinitionFromRequest(request)
                taskDefinitionService.add(taskDefinition)
                resourceTagsRepository.update(taskDefinition.taskDefinitionArn, request.tags)

                RegisterTaskDefinitionResult().withTaskDefinition(taskDefinition)
            }
            else -> throw IllegalArgumentException(request::class.simpleName)
        }
    }
}
