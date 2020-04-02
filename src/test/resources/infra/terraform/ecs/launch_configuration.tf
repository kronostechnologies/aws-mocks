data "aws_ami" "ecs-optimized-ami" {
    most_recent = true
    owners = ["amazon"]

    filter {
        name = "name"
        values = ["amzn-ami-*-amazon-ecs-optimized"]
    }
}

resource "aws_launch_configuration" "main" {
    name = "aws_launch_configuration"
    image_id = data.aws_ami.ecs-optimized-ami.image_id
    instance_type = "t3a.nano"
    security_groups = var.security_group_ids_for_ec2_instances
    iam_instance_profile = "${local.hyphenized_name}-ecs-role"
    user_data_base64 = base64encode("some template")

    lifecycle {
        create_before_destroy = true
    }
}
