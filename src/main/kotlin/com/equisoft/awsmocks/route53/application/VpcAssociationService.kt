package com.equisoft.awsmocks.route53.application

import com.amazonaws.services.route53.model.VPC
import com.equisoft.awsmocks.route53.infrastructure.persistence.VpcAssociationRepository

class VpcAssociationService(
    private val vpcAssociationRepository: VpcAssociationRepository
) {
    fun associate(vpc: VPC, hostedZoneId: String) {
        vpcAssociationRepository.addOrUpdate(hostedZoneId, vpc)
    }

    fun dissociate(vpc: VPC, hostedZoneId: String) {
        vpcAssociationRepository.dissociate(hostedZoneId, vpc)
    }

    fun getAll(hostedZoneId: String): List<VPC>? = vpcAssociationRepository[hostedZoneId]
}
