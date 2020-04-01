resource "aws_route_table" "main" {
    vpc_id = aws_vpc.main.id

    tags = {
        Name = "route table"
    }
}

resource "aws_internet_gateway" "main" {
    vpc_id = aws_vpc.main.id

    tags = {
        Name = "internet gateway"
    }
}

resource "aws_route" "route" {
    route_table_id = aws_route_table.main.id
    destination_cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
}

resource "aws_route_table_association" "association" {
    route_table_id = aws_route_table.main.id
    subnet_id = aws_subnet.main.id
}
