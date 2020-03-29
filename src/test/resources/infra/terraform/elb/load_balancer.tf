resource "aws_lb" "loadbalancer" {
    name = "loadbalancer"
    internal = false
    load_balancer_type = "application"
    security_groups = [var.security_group.id]
    subnets = [var.subnet.id]

    tags = {
        Name = "loadbalancer"
    }
}

resource "aws_lb_target_group" "main" {
    name = "target-group-1"
    port = 80
    protocol = "HTTP"
    vpc_id = var.vpc.id
    target_type = "instance"

    tags = {
        Name = "target group"
    }
}

resource "aws_lb_target_group" "other" {
    name = "target-group-2"
    port = 80
    protocol = "HTTP"
    vpc_id = var.vpc.id
    target_type = "instance"

    tags = {
        Name = "target group"
    }
}

resource "aws_lb_listener" "listener" {
    default_action {
        type = "forward"
        target_group_arn = aws_lb_target_group.main.arn
    }

    load_balancer_arn = aws_lb.loadbalancer.arn
    protocol = "HTTPS"
    port = 443

    ssl_policy = "ELBSecurityPolicy-2016-08"
    certificate_arn = "aws_acm_certificate.arn"
}

resource "aws_lb_target_group" "target_group" {
    port = 80
    protocol = "HTTP"
    vpc_id = var.vpc.id
    target_type = "instance"
    deregistration_delay = 30

    health_check {
        path = "/healtcheck"
        port = "traffic-port"
        protocol = "HTTP"
        matcher = "200"

        interval = 10
        timeout = 10
        healthy_threshold = 2
        unhealthy_threshold = 3
    }

    tags = {
        Name = "loadbalancer"
    }
}

resource "aws_lb_listener_rule" "forward" {
    action {
        type = "forward"
        target_group_arn = aws_lb_target_group.target_group.arn
    }
    condition {
        host_header {
            values = ["google.ca"]
        }
    }
    listener_arn = aws_lb_listener.listener.arn
}

