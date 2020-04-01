resource "aws_kms_key" "key" {
    description = "Some KMS key"
    deletion_window_in_days = 10

    tags = {
        Name = "some-key"
    }
}

resource "aws_kms_alias" "alias" {
    name = "alias/somekey"
    target_key_id = aws_kms_key.key.id
}
