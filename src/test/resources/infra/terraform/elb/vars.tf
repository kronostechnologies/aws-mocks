variable "security_group" {
    type = object({
        id = string
    })
}
variable "subnet" {
    type = object({
        id = string
    })
}

variable "vpc" {
    type = object({
        id = string
    })
}
