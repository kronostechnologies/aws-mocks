package com.equisoft.awsmocks.route53.application

import com.amazonaws.services.route53.model.DelegationSet
import com.amazonaws.services.route53.model.HostedZone
import com.equisoft.awsmocks.common.exceptions.BadRequestException
import com.equisoft.awsmocks.route53.domain.newDefaultDelegationSet
import com.equisoft.awsmocks.route53.infrastructure.persistence.DelegationSetAssociationRepository
import com.equisoft.awsmocks.route53.infrastructure.persistence.DelegationSetRepository

class DelegationSetService(
    private val delegationSetRepository: DelegationSetRepository,
    private val delegationSetAssociationRepository: DelegationSetAssociationRepository
) {
    fun add(delegationSet: DelegationSet) {
        delegationSetRepository[delegationSet.id] = delegationSet
    }

    fun get(id: String): DelegationSet = delegationSetRepository[id]
        ?: newDefaultDelegationSet(id).also(::add)

    fun associate(id: String, hostedZone: HostedZone): DelegationSet {
        val delegationSet = get(id)

        delegationSetAssociationRepository.addOrUpdate(id, hostedZone)

        return delegationSet
    }

    fun dissociate(hostedZone: HostedZone) {
        delegationSetAssociationRepository.removeHostedZone(hostedZone)
    }

    fun findForZone(hostedZone: HostedZone): DelegationSet? =
        delegationSetAssociationRepository.find(hostedZone)?.let(::get)

    fun delete(id: String?) {
        val associatedZones = delegationSetAssociationRepository[id]
        if (associatedZones.isNullOrEmpty()) {
            delegationSetAssociationRepository.remove(id)
            delegationSetRepository.remove(id)
        } else {
            throw BadRequestException("DelegationSetInUse")
        }
    }
}
