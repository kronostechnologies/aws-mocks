resource "aws_kms_key" "encrypt_key" {
    description = "Key used to encrypt"
}

resource "aws_kms_ciphertext" "ciphertext" {
    plaintext = "some text to encrypt"
    key_id = aws_kms_key.encrypt_key.arn
}
