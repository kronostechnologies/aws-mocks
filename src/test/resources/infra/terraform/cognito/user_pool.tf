resource "aws_cognito_user_pool" "main" {
    name = "some-user-pool"

    tags = {
        Name = "Some User Pool"
    }
}
