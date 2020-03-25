resource "aws_cognito_user_pool_client" "app_client" {
    name = "some-client"
    user_pool_id = aws_cognito_user_pool.main.id
    generate_secret = true
    allowed_oauth_flows_user_pool_client = true
    allowed_oauth_flows = ["client_credentials"]
    allowed_oauth_scopes = aws_cognito_resource_server.resource.scope_identifiers
}

resource "aws_cognito_user_pool_client" "app_client1" {
    name = "some-client1"
    user_pool_id = aws_cognito_user_pool.main.id
    generate_secret = true
    allowed_oauth_flows_user_pool_client = true
    allowed_oauth_flows = ["client_credentials"]
    allowed_oauth_scopes = aws_cognito_resource_server.resource.scope_identifiers
}

resource "aws_cognito_user_pool_client" "app_client2" {
    name = "some-client2"
    user_pool_id = aws_cognito_user_pool.main.id
    generate_secret = true
    allowed_oauth_flows_user_pool_client = true
    allowed_oauth_flows = ["client_credentials"]
    allowed_oauth_scopes = aws_cognito_resource_server.resource.scope_identifiers
}

resource "aws_cognito_user_pool_client" "app_client3" {
    name = "some-client3"
    user_pool_id = aws_cognito_user_pool.main.id
    generate_secret = true
    allowed_oauth_flows_user_pool_client = true
    allowed_oauth_flows = ["client_credentials"]
    allowed_oauth_scopes = aws_cognito_resource_server.resource.scope_identifiers
}

resource "aws_cognito_user_pool_client" "app_client4" {
    name = "some-client4"
    user_pool_id = aws_cognito_user_pool.main.id
    generate_secret = true
    allowed_oauth_flows_user_pool_client = true
    allowed_oauth_flows = ["client_credentials"]
    allowed_oauth_scopes = aws_cognito_resource_server.resource.scope_identifiers
}

