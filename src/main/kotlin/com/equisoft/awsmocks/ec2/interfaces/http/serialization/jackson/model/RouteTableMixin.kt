package com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.model

import com.amazonaws.services.ec2.model.PropagatingVgw
import com.amazonaws.services.ec2.model.Route
import com.amazonaws.services.ec2.model.RouteTableAssociation
import com.amazonaws.services.ec2.model.Tag
import com.equisoft.awsmocks.ec2.interfaces.http.serialization.jackson.ListItem

interface RouteTableMixin {
    @ListItem("tagSet")
    fun getTags(): List<Tag>

    @ListItem("routeSet")
    fun getRoutes(): List<Route>

    @ListItem("associationSet")
    fun getAssociations(): List<RouteTableAssociation>

    @ListItem("propagatingVgwSet")
    fun getPropagatingVgws(): List<PropagatingVgw>
}
