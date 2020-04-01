package com.equisoft.awsmocks.ecs.application

import com.amazonaws.services.ecs.model.Cluster
import com.amazonaws.services.ecs.model.Failure
import com.amazonaws.services.ecs.model.Tag
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.common.infrastructure.aws.AwsService.ECS
import com.equisoft.awsmocks.common.infrastructure.aws.isArn
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ecs.infrastructure.persistence.ClusterRepository

class ClusterService(
    private val clusterRepository: ClusterRepository,
    private val tagsRepository: ResourceTagsRepository<Tag>
) {
    fun add(cluster: Cluster) {
        clusterRepository[cluster.clusterName] = cluster
    }

    fun get(clusterName: String): Cluster =
        clusterRepository[clusterName] ?: throw NotFoundException()

    fun getAll(clusters: List<String>, includeFields: List<String>): SearchResult<Cluster> {
        val clusterSearchResult = SearchResult<Cluster>()

        clusters.forEach { value ->
            if (isArn(value)) {
                clusterRepository.findByArn(value)?.run { clusterSearchResult.withFound(this) }
                    ?: clusterSearchResult.withFailures(Failure().withArn(value))
            } else {
                clusterRepository[value]?.run { clusterSearchResult.withFound(this) }
                    ?: clusterSearchResult.withFailures(
                        Failure().withArn(ECS.createArn(null, "cluster/$value")))
            }
        }

        return clusterSearchResult
    }
}
