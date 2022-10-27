package com.equisoft.awsmocks.route53.application

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.AmazonWebServiceResult
import com.amazonaws.ResponseMetadata
import com.amazonaws.services.route53.model.AssociateVPCWithHostedZoneRequest
import com.amazonaws.services.route53.model.AssociateVPCWithHostedZoneResult
import com.amazonaws.services.route53.model.ChangeInfo
import com.amazonaws.services.route53.model.ChangeResourceRecordSetsRequest
import com.amazonaws.services.route53.model.ChangeResourceRecordSetsResult
import com.amazonaws.services.route53.model.ChangeStatus
import com.amazonaws.services.route53.model.ChangeTagsForResourceRequest
import com.amazonaws.services.route53.model.ChangeTagsForResourceResult
import com.amazonaws.services.route53.model.CreateHostedZoneRequest
import com.amazonaws.services.route53.model.CreateReusableDelegationSetRequest
import com.amazonaws.services.route53.model.CreateReusableDelegationSetResult
import com.amazonaws.services.route53.model.DelegationSet
import com.amazonaws.services.route53.model.DeleteReusableDelegationSetRequest
import com.amazonaws.services.route53.model.DeleteReusableDelegationSetResult
import com.amazonaws.services.route53.model.DisassociateVPCFromHostedZoneRequest
import com.amazonaws.services.route53.model.DisassociateVPCFromHostedZoneResult
import com.amazonaws.services.route53.model.GetReusableDelegationSetRequest
import com.amazonaws.services.route53.model.GetReusableDelegationSetResult
import com.amazonaws.services.route53.model.HostedZone
import com.amazonaws.services.route53.model.ListHostedZonesRequest
import com.amazonaws.services.route53.model.ListHostedZonesResult
import com.amazonaws.services.route53.model.ListResourceRecordSetsRequest
import com.amazonaws.services.route53.model.ListResourceRecordSetsResult
import com.amazonaws.services.route53.model.ListTagsForResourceRequest
import com.amazonaws.services.route53.model.ListTagsForResourceResult
import com.amazonaws.services.route53.model.ResourceRecordSet
import com.amazonaws.services.route53.model.ResourceTagSet
import com.amazonaws.services.route53.model.Tag
import com.amazonaws.services.route53.model.TagResourceType
import com.amazonaws.services.route53.model.UpdateHostedZoneCommentRequest
import java.util.UUID

@SuppressWarnings("LongMethod", "ComplexMethod")
class Route53RequestHandler(
    private val delegationSetService: DelegationSetService,
    private val hostedZoneService: HostedZoneService,
    private val vpcAssociationService: VpcAssociationService
) {
    fun handle(
        request: AmazonWebServiceRequest,
        parameters: Map<String, List<String>> = mapOf()
    ): AmazonWebServiceResult<ResponseMetadata> {
        val id: String? = parameters["id"]?.firstOrNull()

        return when (request) {
            is AssociateVPCWithHostedZoneRequest -> {
                vpcAssociationService.associate(request.vpc, request.hostedZoneId)

                val changeInfo: ChangeInfo = ChangeInfo()
                    .withId("/change/${UUID.randomUUID()}")
                    .withStatus(ChangeStatus.INSYNC)
                AssociateVPCWithHostedZoneResult().withChangeInfo(changeInfo)
            }
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
                request.vpc?.let { vpcAssociationService.associate(it, hostedZone.id) }

                val delegationSet: DelegationSet? = request.delegationSetId?.let {
                    delegationSetService.associate(it, hostedZone)
                }

                createHostedZoneResult(hostedZone).withDelegationSet(delegationSet).withVPC(request.vpc)
            }
            is DeleteReusableDelegationSetRequest -> {
                delegationSetService.delete(id)

                DeleteReusableDelegationSetResult()
            }
            is DisassociateVPCFromHostedZoneRequest -> {
                vpcAssociationService.dissociate(request.vpc, request.hostedZoneId)

                val changeInfo: ChangeInfo = ChangeInfo()
                    .withId("/change/${UUID.randomUUID()}")
                    .withStatus(ChangeStatus.INSYNC)
                DisassociateVPCFromHostedZoneResult().withChangeInfo(changeInfo)
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
