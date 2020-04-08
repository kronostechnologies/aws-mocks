resource "aws_autoscaling_notification" "ecs_autoscaling" {
    group_names = [aws_autoscaling_group.main.name]

    notifications = [
        "autoscaling:EC2_INSTANCE_LAUNCH",
        "autoscaling:EC2_INSTANCE_TERMINATE",
        "autoscaling:EC2_INSTANCE_LAUNCH_ERROR",
        "autoscaling:EC2_INSTANCE_TERMINATE_ERROR",
    ]

    topic_arn = "arn:aws:::topic"
}
