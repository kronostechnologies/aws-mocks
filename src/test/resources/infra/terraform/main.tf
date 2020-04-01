provider "aws" {
    region = "us-east-1"
    access_key = "1234567890"
    secret_key = "unused"

    endpoints {
        acm = "http://${var.hostname}:4501"
        cognitoidp = "http://${var.hostname}:4500"
        ec2 = "http://${var.hostname}:4503"
        kms = "http://${var.hostname}:4502"
        route53 = "http://${var.hostname}:4580"
    }

    s3_force_path_style = true
    skip_credentials_validation = true
    skip_metadata_api_check = true
    skip_requesting_account_id = true
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
