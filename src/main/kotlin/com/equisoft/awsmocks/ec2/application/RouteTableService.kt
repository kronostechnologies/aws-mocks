package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.Route
import com.amazonaws.services.ec2.model.RouteTable
import com.amazonaws.services.ec2.model.RouteTableAssociation
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.RouteTableRepository

class RouteTableService(
    routeTableRepository: RouteTableRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseEc2Service<RouteTable, RouteTableRepository>({ it.routeTableId }, routeTableRepository, tagsRepository) {
    override val notFoundMessage: String = "InvalidRouteTableID.NotFound"

    override fun withTags(value: RouteTable, tags: Collection<Tag>): RouteTable = value.withTags(tags)

    fun addAssociation(routeTableAssociation: RouteTableAssociation) {
        val routeTable: RouteTable = get(routeTableAssociation.routeTableId)

        routeTable.associations.add(routeTableAssociation)

        addOrUpdate(routeTable)
    }

    fun removeAssociation(associationId: String) {
        repository.values.forEach { routeTable: RouteTable ->
            routeTable.associations.removeIf { it.routeTableAssociationId == associationId }
        }
    }

    fun addRoute(routeTableId: String, route: Route) {
        val routeTable: RouteTable = get(routeTableId)

        routeTable.routes.add(route)

        addOrUpdate(routeTable)
    }
}
