resource "aws_cognito_resource_server" "resource" {
    identifier = "somresource"
    name = "somresource"
    user_pool_id = aws_cognito_user_pool.main.id

    scope {
        scope_description = "Some Scope"
        scope_name = "some.scope"
    }
}
