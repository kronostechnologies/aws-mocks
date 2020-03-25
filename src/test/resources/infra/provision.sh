#!/usr/bin/env sh

set -e

BASEDIR=$(dirname "$0")
cd "$BASEDIR"/terraform

TF_DATA_DIR=../../.terraform terraform init
TF_DATA_DIR=../../.terraform terraform apply -auto-approve -state=../../terraform.tfstate
