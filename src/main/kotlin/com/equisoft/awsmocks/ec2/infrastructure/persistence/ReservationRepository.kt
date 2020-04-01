package com.equisoft.awsmocks.ec2.infrastructure.persistence

import com.amazonaws.services.ec2.model.Filter
import com.amazonaws.services.ec2.model.Reservation
import com.equisoft.awsmocks.ec2.domain.applyFilters

class ReservationRepository : BaseEc2Repository<String, Reservation>() {
    @Synchronized
    override fun find(filters: List<Filter>): List<Reservation> = values.toList().applyFilters(filters)
}
