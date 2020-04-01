output "vpc" {
    value = aws_vpc.main
}

output "subnet" {
    value = aws_subnet.main
}

output "security_group" {
    value = aws_security_group.main
}
