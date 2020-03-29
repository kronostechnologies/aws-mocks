resource "aws_autoscaling_group" "main" {
    name = "autoscaling-group"
    launch_configuration = "aws_launch_configuration"

    min_size = 1
    max_size = 1
    desired_capacity = 1

    vpc_zone_identifier = var.subnet_ids

    health_check_type = "ELB"
    health_check_grace_period = 30

    tag {
        key = "a-tag"
        propagate_at_launch = true
        value = "a-value"
    }

    tag {
        key = "another-tag"
        propagate_at_launch = true
        value = "a-value"
    }
}
