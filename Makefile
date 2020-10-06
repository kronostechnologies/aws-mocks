.PHONY: build_image test_infra_init test_infra test_infra_plan

build_image:
	docker build -t ghcr.io/kronostechnologies/aws-mocks .

test_infra_init:
	cd src/test/resources/infra/terraform && TF_DATA_DIR=../../.terraform terraform init

test_infra: test_infra_init
	rm -f ./src/test/resources/terraform.tfstate*
	cd src/test/resources/infra/terraform && TF_DATA_DIR=../../.terraform terraform apply -auto-approve -state=../../terraform.tfstate

test_infra_plan: test_infra_init
	cd src/test/resources/infra/terraform && TF_DATA_DIR=../../.terraform terraform plan -state=../../terraform.tfstate

test_infra_apply: test_infra_init
	cd src/test/resources/infra/terraform && TF_DATA_DIR=../../.terraform terraform apply -auto-approve -state=../../terraform.tfstate

test_infra_destroy: test_infra_init
	cd src/test/resources/infra/terraform && TF_DATA_DIR=../../.terraform terraform destroy -target=${TF_DESTROY_TARGET} -state=../../terraform.tfstate
