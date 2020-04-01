package com.equisoft.awsmocks.route53.application

import com.amazonaws.services.route53.model.ChangeBatch
import com.amazonaws.services.route53.model.ChangeTagsForResourceRequest
import com.amazonaws.services.route53.model.HostedZone
import com.amazonaws.services.route53.model.ResourceRecordSet
import com.amazonaws.services.route53.model.Tag
import com.amazonaws.services.route53.model.UpdateHostedZoneCommentRequest
import com.equisoft.awsmocks.common.exceptions.NotFoundException
import com.equisoft.awsmocks.common.infrastructure.persistence.ResourceTagsRepository
import com.equisoft.awsmocks.route53.infrastructure.persistence.HostedZoneRepository
import com.equisoft.awsmocks.route53.infrastructure.persistence.RecordSetRepository

class HostedZoneService(
    private val delegationSetService: DelegationSetService,
    private val hostedZoneRepository: HostedZoneRepository,
    private val resourceTagsRepository: ResourceTagsRepository<Tag>,
    private val recordSetRepository: RecordSetRepository
) {
    fun addOrUpdateZone(hostedZone: HostedZone) {
        hostedZoneRepository[hostedZone.id] = hostedZone
    }

    fun get(id: String): HostedZone = hostedZoneRepository[id] ?: throw NotFoundException("NoSuchHostedZone")

    fun updateComment(request: UpdateHostedZoneCommentRequest): HostedZone = hostedZoneRepository[request.id]!!
        .also { it.config.comment = request.comment }

    fun changeTags(id: String, request: ChangeTagsForResourceRequest) {
        val hostedZone: HostedZone = get(id)
        resourceTagsRepository.update(hostedZone.id, request.addTags, request.removeTagKeys)
    }

    fun getTags(id: String): List<Tag> {
        val hostedZone: HostedZone = get(id)
        return resourceTagsRepository.getOrDefault(hostedZone.id, listOf())
    }

    fun getAll(): Collection<HostedZone> = hostedZoneRepository.values

    fun delete(id: String) {
        val hostedZone = get(id)
        delegationSetService.dissociate(hostedZone)
        hostedZoneRepository.remove(id)
    }

    fun changeRecordsSets(id: String, changeBatch: ChangeBatch) {
        val hostedZone: HostedZone = get(id)
        changeBatch.changes.forEach {
            when (it.action) {
                "CREATE", "UPSERT" -> {
                    it.resourceRecordSet.name = ensureDot(it.resourceRecordSet.name)
                    recordSetRepository.addOrUpdate(hostedZone.id, it.resourceRecordSet)
                }
                "DELETE" -> recordSetRepository.remove(hostedZone.id, it.resourceRecordSet)
            }
        }
    }

    private fun ensureDot(name: String) = name + if (name.endsWith(".")) "" else "."

    fun getRecordSets(id: String): List<ResourceRecordSet> {
        val hostedZone = get(id)
        return recordSetRepository[hostedZone.id].orEmpty()
    }
}
