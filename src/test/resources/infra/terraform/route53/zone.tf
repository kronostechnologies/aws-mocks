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

resource "aws_route53_zone" "private_zone" {
    name = "privatezone.ca"
    comment = "private zone"

    vpc {
        vpc_id = "vpc_id"
        vpc_region = "us-east-1"
    }
}
