provider "aws" {
    region = "us-east-1"
    access_key = "12345678901234"
    secret_key = "unused"

    endpoints {
        acm = "http://${var.hostname}:4501"
        autoscaling = "http://${var.hostname}:4506"
        cognitoidp = "http://${var.hostname}:4500"
        ec2 = "http://${var.hostname}:4503"
        ecs = "http://${var.hostname}:4504"
        elb = "http://${var.hostname}:4505"
        kms = "http://${var.hostname}:4502"
        route53 = "http://${var.hostname}:4580"
    }

    s3_force_path_style = true
    skip_credentials_validation = true
    skip_metadata_api_check = true
    skip_requesting_account_id = true
}

module "autoscaling" {
    source = "./autoscaling"

    subnet_ids = [module.ec2.subnet.id]
}

module "ec2" {
    source = "./ec2"
}

module "ecs" {
    source = "./ecs"

    vpc_id = module.ec2.vpc.id
    subnet_ids = [module.ec2.subnet.id]
    security_group_ids_for_ec2_instances = [module.ec2.security_group.id]

    desired_instance_count = 1
    minimum_instances = 1
    maximum_instances = 1

    autoscaling_group_name = module.autoscaling.autoscaling_group.name
    target_groups_arns = module.elb.lb_target_groups_arns
}

module "elb" {
    source = "./elb"

    security_group = module.ec2.security_group
    subnet = module.ec2.subnet
    vpc = module.ec2.vpc
}

module "kms" {
    source = "./kms"
}

module "cognito" {
    source = "./cognito"
}

module "route53" {
    source = "./route53"
}
