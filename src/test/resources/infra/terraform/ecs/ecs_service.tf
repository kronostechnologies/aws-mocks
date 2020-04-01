resource "aws_ecs_task_definition" "task" {
    family = local.container_name
    container_definitions = file("${path.module}/service.json")
    memory = 1024
    task_role_arn = "arn:aws::::task/role"

    network_mode = "bridge"

    tags = {
        Name = local.container_name
    }
}

resource "aws_ecs_service" "service" {
    name = local.container_name
    cluster = aws_ecs_cluster.cluster.id
    task_definition = aws_ecs_task_definition.task.arn
    launch_type = "EC2"
    desired_count = 1
    iam_role = "arn:aws:iam::123456789012:role/SomeRole"

    load_balancer {
        target_group_arn = "arn:aws::::target_group_arn"
        container_name = local.container_name
        container_port = 80
    }

    tags = {
        Name = local.container_name
    }
}

resource "aws_autoscaling_attachment" "autoscaling_target_group" {
    count = length(var.target_groups_arns)
    autoscaling_group_name = var.autoscaling_group_name
    alb_target_group_arn = var.target_groups_arns[count.index]
}

