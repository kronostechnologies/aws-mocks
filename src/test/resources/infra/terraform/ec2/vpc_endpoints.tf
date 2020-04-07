data "aws_vpc_endpoint_service" "dynamo" {
    service = "dynamodb"
}

resource "aws_vpc_endpoint" "dynamo" {
    count = 12
    service_name = data.aws_vpc_endpoint_service.dynamo.service_name
    vpc_id = aws_vpc.main.id

    tags = {
        Name = "DynamoDB VPC Endpoint"
    }
}

resource "aws_vpc_endpoint_route_table_association" "dynamo" {
    count = 12
    route_table_id = aws_route_table.main.id
    vpc_endpoint_id = aws_vpc_endpoint.dynamo[count.index].id
}
