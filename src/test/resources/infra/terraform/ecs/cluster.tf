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
