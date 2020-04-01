package com.equisoft.awsmocks.ec2.application

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.GroupIdentifier
import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.Reservation
import com.amazonaws.services.ec2.model.Subnet
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.ec2.infrastructure.persistence.ReservationRepository

class ReservationService(
    private val instanceService: InstanceService,
    private val securityGroupService: SecurityGroupService,
    private val subnetService: SubnetService,
    reservationRepository: ReservationRepository,
    tagsRepository: ResourceTagsRepository<Tag>
) : BaseEc2Service<Reservation, ReservationRepository>({ it.reservationId }, reservationRepository, tagsRepository) {
    override val notFoundMessage: String = "InvalidReservationID.NotFound"

    override fun addOrUpdate(value: Reservation) {
        super.addOrUpdate(value)

        updateSecurityGroupsNames(value)
        value.instances.forEach { instance ->
            updateVpcDetails(instance)
            instanceService.addOrUpdate(instance)
        }
    }

    override fun getAll(ids: List<String>?, filters: List<Filter>?): List<Reservation> {
        return super.getAll(ids, filters).onEach(this::updateSecurityGroupsNames)
    }

    override fun withTags(value: Reservation, tags: Collection<Tag>): Reservation = value

    private fun updateVpcDetails(instance: Instance) {
        if (instance.subnetId != null) {
            val subnet: Subnet = subnetService.get(instance.subnetId)
            instance.vpcId = subnet.vpcId
        }

        instance.networkInterfaces.forEach { networkInterface ->
            if (networkInterface.subnetId != null) {
                val subnet: Subnet = subnetService.get(networkInterface.subnetId)
                networkInterface.vpcId = subnet.vpcId
            }
        }
    }

    private fun updateSecurityGroupsNames(reservation: Reservation) {
        reservation.groups.setNames()
        reservation.instances.forEach { instance ->
            instance.securityGroups.setNames()
            instance.networkInterfaces.forEach { networkInterface ->
                networkInterface.groups.setNames()
            }
        }
    }

    private fun List<GroupIdentifier>.setNames() {
        filter { it.groupId != null }
            .forEach {
                val securityGroup = securityGroupService.get(it.groupId)
                it.groupName = securityGroup.groupName
            }
    }
}
