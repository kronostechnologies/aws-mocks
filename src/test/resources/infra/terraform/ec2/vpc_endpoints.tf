data "aws_vpc_endpoint_service" "dynamo" {
    service = "dynamodb"
}

resource "aws_vpc_endpoint" "dynamo" {
    count = 2
    service_name = data.aws_vpc_endpoint_service.dynamo.service_name
    vpc_id = aws_vpc.main.id

    tags = {
        Name = "DynamoDB VPC Endpoint"
    }
}
