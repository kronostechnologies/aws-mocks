data "aws_ami" "ecs-optimized-ami" {
    most_recent = true
    owners = ["amazon"]

    filter {
        name = "name"
        values = ["amzn-ami-*-amazon-ecs-optimized"]
    }
}
