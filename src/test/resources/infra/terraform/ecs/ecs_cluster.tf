data "template_file" "ecs_instance_policy" {
    template = file("${path.module}/ecs-instance-policy.json")

    vars = {
        region = local.aws_region
        accountId = local.aws_account_id
        clusterName = aws_ecs_cluster.cluster.name
    }
}

resource "aws_ecs_cluster" "cluster" {
    name = local.hyphenized_name
    setting {
        name = "containerInsights"
        value = local.container_insights
    }

    tags = {
        Name = local.cluster_name
    }
}
