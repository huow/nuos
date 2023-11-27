provider "aws" {
  region = "us-east-1"
}

resource "aws_vpc" "nous_vpc" {
  cidr_block = "10.0.0.0/16"
}

# use data source to get all availability zones in region
data "aws_availability_zones" "available_zones" {}