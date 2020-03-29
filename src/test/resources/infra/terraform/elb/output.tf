output "lb_target_groups_arns" {
    value = [aws_lb_target_group.target_group.arn, aws_lb_target_group.other.arn]
}
