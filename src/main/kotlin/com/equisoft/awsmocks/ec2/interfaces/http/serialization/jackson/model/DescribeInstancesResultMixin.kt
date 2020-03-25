package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.Reservation
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "DescribeInstancesResponse")
interface DescribeInstancesResultMixin {
    @ListItem("reservationSet")
    fun getReservations(): List<Reservation>
}
