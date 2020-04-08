resource "aws_autoscaling_group" "ecs" {
    name = "${local.hyphenized_name}-scaling-group"
    launch_configuration = aws_launch_configuration.main.name

    min_size = var.minimum_instances
    max_size = var.maximum_instances
    desired_capacity = var.desired_instance_count

    vpc_zone_identifier = var.subnet_ids

    health_check_type = "ELB"
    health_check_grace_period = 30
}

resource "aws_ecs_capacity_provider" "capacity_provider" {
    name = "${local.hyphenized_name}-capacity-provider"

    auto_scaling_group_provider {
        auto_scaling_group_arn = aws_autoscaling_group.ecs.arn
        managed_scaling {
            status = "ENABLED"
            minimum_scaling_step_size = 1
            maximum_scaling_step_size = 1
            target_capacity = 75
        }
    }

    tags = {
        Name = "${local.hyphenized_name}-capacity-provider"
    }
}
