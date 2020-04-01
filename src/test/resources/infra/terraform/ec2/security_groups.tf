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

    egress {
        from_port = 443
        to_port = 443
        protocol = "tcp"

        cidr_blocks = ["0.0.0.0/0"]
    }

    tags = {
        Name = "Loadbalancer"
    }
}
