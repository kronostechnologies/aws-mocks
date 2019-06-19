package com.equisoft.awsmocks.route53.context

import com.amazonaws.services.route53.model.HostedZone
import com.amazonaws.services.route53.model.HostedZoneConfig
import com.equisoft.awsmocks.common.context.Route53Environment
import com.equisoft.awsmocks.route53.infrastructure.persistence.HostedZoneRepository
import com.uchuhimo.konf.Config
import java.util.UUID

class Route53Bootstrapper(
    hostedZoneRepository: HostedZoneRepository,
    config: Config
) {
    init {
        config[Route53Environment.domain]?.let { zoneId ->
            val hostedZone: HostedZone = HostedZone(zoneId, "$zoneId.", UUID.randomUUID().toString())
                .withConfig(HostedZoneConfig().withPrivateZone(false))
            hostedZoneRepository.put(zoneId, hostedZone)
        }
    }
}
