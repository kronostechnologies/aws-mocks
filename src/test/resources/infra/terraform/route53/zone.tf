resource "aws_route53_delegation_set" "main" {
    reference_name = "delegation set"
}

resource "aws_route53_zone" "zone" {
    name = "zone.ca"
    comment = "zone"
    delegation_set_id = aws_route53_delegation_set.main.id

    tags = {
        Name = "zone"
    }
}
