locals {
    cluster_name = "Cluster"
    container_insights = var.enable_container_insights ? "enabled" : "disabled"
    hyphenized_name = replace(local.cluster_name, "/\\s+/", "-")

    container_name = "container_name"

    aws_account_id = "1234567890"
    aws_region = "us-east-1"
}

variable "vpc_id" {
    type = string
}

variable "subnet_ids" {
    type = list(string)
}

variable "security_group_ids_for_ec2_instances" {
    type = list(string)
    description = "This security group will be attached to all created EC2 instances"
}

variable "instance_type" {
    type = string
    default = "t3a.micro"
}

variable "minimum_instances" {
    type = string
    default = 1
}

variable "maximum_instances" {
    type = string
    default = 1
}

variable "desired_instance_count" {
    type = string
    default = 1
}

variable "enable_container_insights" {
    description = "Enable Container Insights"
    type = bool
    default = true
}

variable "autoscaling_group_name" {
    type = string
}

variable "target_groups_arns" {
    type = list(string)
}
