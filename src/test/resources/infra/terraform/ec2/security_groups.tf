resource "aws_security_group" "main" {
    name = "loadbalancer"
    description = "load balancer"
    vpc_id = aws_vpc.main.id

    ingress {
        from_port = 443
        to_port = 443
        protocol = "tcp"

        cidr_blocks = ["0.0.0.0/0"]
    }

    ingress {
        from_port = 80
        to_port = 80
        protocol = "tcp"

        security_groups = ["a_security_group"]
    }

    egress {
        from_port = 443
        to_port = 443
        protocol = "tcp"

        cidr_blocks = ["0.0.0.0/0"]
    }

    egress {
        from_port = 0
        protocol = "-1"
        to_port = 0
        prefix_list_ids = ["pl-123456"]
    }

    tags = {
        Name = "Loadbalancer"
    }
}
