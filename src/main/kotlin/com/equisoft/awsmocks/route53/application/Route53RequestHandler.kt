package com.equisoft.awsmocks.route53.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.route53.model.*
import java.util.UUID

@SuppressWarnings("LongMethod")
class Route53RequestHandler(
    private val delegationSetService: DelegationSetService,
    private val hostedZoneService: HostedZoneService
) {
    fun handle(
        request: AmazonWebServiceRequest,
        parameters: Map<String, List<String>> = mapOf()
    ): AmazonWebServiceResult<ResponseMetadata> {
        val id: String? = parameters["id"]?.firstOrNull()

        return when (request) {
            is ChangeResourceRecordSetsRequest -> {
                hostedZoneService.changeRecordsSets(id!!, request.changeBatch)

                val changeInfo: ChangeInfo = ChangeInfo()
                    .withId("/change/${UUID.randomUUID()}")
                    .withStatus(ChangeStatus.INSYNC)
                ChangeResourceRecordSetsResult().withChangeInfo(changeInfo)
            }
            is ChangeTagsForResourceRequest -> {
                hostedZoneService.changeTags(id!!, request)
                ChangeTagsForResourceResult()
            }
            is CreateReusableDelegationSetRequest -> {
                val delegationSet: DelegationSet = createDelegationSetFromRequest(request)
                delegationSetService.add(delegationSet)

                CreateReusableDelegationSetResult().withDelegationSet(delegationSet)
            }
            is CreateHostedZoneRequest -> {
                val hostedZone: HostedZone = createHostedZoneFromRequest(request)
                hostedZoneService.addOrUpdateZone(hostedZone)

                val delegationSet: DelegationSet? = request.delegationSetId?.let {
                    delegationSetService.associate(it, hostedZone)
                }

                createHostedZoneResult(hostedZone).withDelegationSet(delegationSet)
            }
            is DeleteReusableDelegationSetRequest -> {
                delegationSetService.delete(id)

                DeleteReusableDelegationSetResult()
            }
            is GetReusableDelegationSetRequest -> {
                val delegationSet: DelegationSet = delegationSetService.get(id!!)

                GetReusableDelegationSetResult().withDelegationSet(delegationSet)
            }
            is ListHostedZonesRequest -> {
                val hostedZones: Collection<HostedZone> = hostedZoneService.getAll()

                ListHostedZonesResult().withHostedZones(hostedZones).withIsTruncated(false)
            }
            is ListResourceRecordSetsRequest -> {
                val records: List<ResourceRecordSet> = hostedZoneService.getRecordSets(id!!)

                ListResourceRecordSetsResult().withResourceRecordSets(records).withIsTruncated(false)
            }
            is ListTagsForResourceRequest -> {
                val tags: List<Tag> = hostedZoneService.getTags(id!!)
                val resourceTagSet: ResourceTagSet = ResourceTagSet()
                    .withTags(tags)
                    .withResourceId(id)
                    .withResourceType(TagResourceType.Hostedzone)

                ListTagsForResourceResult().withResourceTagSet(resourceTagSet)
            }
            is UpdateHostedZoneCommentRequest -> {
                val hostedZone: HostedZone = hostedZoneService.updateComment(request)
                updateCommentResult(hostedZone)
            }
            else -> throw IllegalArgumentException(request::class.simpleName)
        }
    }
}
