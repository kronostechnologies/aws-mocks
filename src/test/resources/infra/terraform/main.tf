provider "aws" {
    region = "us-east-1"
    access_key = "12345678901234"
    secret_key = "unused"

    endpoints {
        acm = "http://${var.hostname}:4501"
        autoscaling = "http://${var.hostname}:4506"
        cognitoidp = "http://${var.hostname}:4500"
        ec2 = "http://${var.hostname}:4503"
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
