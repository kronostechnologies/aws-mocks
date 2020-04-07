data "aws_availability_zones" "availability_zones" {
    state = "available"
}

locals {
    availability_zones_names_list = slice(sort([for name in data.aws_availability_zones.availability_zones.names: name]), 0, 1)
    availability_zones_names = toset([for name in local.availability_zones_names_list: name])
}

resource "aws_vpc" "main" {
    cidr_block = "10.0.0.0/16"
    enable_dns_hostnames = true
    enable_dns_support = true
    instance_tenancy = "default"

    tags = {
        Name = "main vpc"
    }
}

resource "aws_subnet" "main" {
    vpc_id = aws_vpc.main.id
    cidr_block = "10.0.0.0/16"
    availability_zone = "az-1"

    tags = {
        Name = "subnet"
    }
}

resource "aws_subnet" "az_subnets" {
    for_each = toset(data.aws_availability_zones.availability_zones.names)
    availability_zone = each.key
    cidr_block = "10.0.0.0/16"
    vpc_id = aws_vpc.main.id
}
