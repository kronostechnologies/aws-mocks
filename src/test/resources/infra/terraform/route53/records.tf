resource "aws_route53_record" "record" {
    name = "www.zone.ca"
    type = "A"
    zone_id = aws_route53_zone.zone.id

    alias {
        evaluate_target_health = false
        name = aws_lb.route.dns_name
        zone_id = aws_lb.route.zone_id
    }
}

resource "aws_lb" "route" {
    name = "loadbalancer-for-route"
    internal = false
    load_balancer_type = "application"

    tags = {
        Name = "loadbalancer-for-route"
    }
}

resource "aws_route53_record" "subdomain_ns" {
    name = "subdomain.zone.ca"
    type = "NS"
    zone_id = aws_route53_zone.zone.id
    records = aws_route53_delegation_set.main.name_servers
    ttl = 86400
}
