data "aws_ami" "nat_instance" {
    most_recent = true
    owners = ["amazon"]

    filter {
        name = "name"
        values = ["amzn-ami-vpc-nat*"]
    }
}

resource "aws_instance" "instance" {
    ami = data.aws_ami.nat_instance.id
    instance_type = "t3a.nano"

    associate_public_ip_address = true
    availability_zone = "az-1"
    subnet_id = aws_subnet.main.id
    source_dest_check = false

    vpc_security_group_ids = [aws_security_group.nat_traffic.id]

    tags = {
        Name = "NAT"
    }

    lifecycle {
        create_before_destroy = true
    }
}

resource "aws_security_group" "nat_traffic" {
    name = "NAT instances"
    description = "Allows private subnets to have access to internet through NAT"
    vpc_id = aws_vpc.main.id

    ingress {
        protocol = -1
        from_port = 0
        to_port = 0
        cidr_blocks = [aws_subnet.main.cidr_block]
    }

    egress {
        protocol = -1
        from_port = 0
        to_port = 0
        cidr_blocks = ["0.0.0.0/0"]
    }
}
