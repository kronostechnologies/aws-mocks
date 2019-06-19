# aws-mocks
This project aims at providing endpoints matching AWS API's to be able to test Terraform provisioning and create mock environments.
It uses [Localstack](https://github.com/localstack/localstack) to mock most services, but adds a OAuth server matching [Amazon Cognito User Pools Auth API](https://docs.aws.amazon.com/cognito/latest/developerguide/cognito-userpools-server-contract-reference.html) and support for those services :
- ACM
- Cognito-idp
- EC2
- KMS
- Route53

## Overview
This is based on [Localstack default ports configuration](https://github.com/localstack/localstack#overview).  
**From this project:**
- Cognito-ido and Cognito Auth at http://localhost:4500
- ACM at http://localhost:4501
- KMS at http://localhost:4502
- EC2 at http://localhost:4503
- Route53 at http://localhost:4580
  
**From Localstack:**
- API Gateway at http://localhost:4567
- Kinesis at http://localhost:4568
- DynamoDB at http://localhost:4569
- DynamoDB Streams at http://localhost:4570
- Elasticsearch at http://localhost:4571
- S3 at http://localhost:4572
- Firehose at http://localhost:4573
- Lambda at http://localhost:4574
- SNS at http://localhost:4575
- SQS at http://localhost:4576
- Redshift at http://localhost:4577
- ES (Elasticsearch Service) at http://localhost:4578
- SES at http://localhost:4579
- CloudFormation at http://localhost:4581
- CloudWatch at http://localhost:4582
- SSM at http://localhost:4583
- SecretsManager at http://localhost:4584
- StepFunctions at http://localhost:4585
- CloudWatch Logs at http://localhost:4586
- STS at http://localhost:4592
- IAM at http://localhost:4593

## Usage
`docker run --rm -it kronostechnologies/aws-mocks`

## Configuration
All the services from this project are started. At the moment this can't be configured.
To configure Localstack services, use the `SERVICES` environment variable.
Check [Localstack configuration section](https://github.com/localstack/localstack#configurations) for more details and other configurations options.

## Supported requests
### ACM
* [x] [AddTagsToCertificateRequest](https://docs.aws.amazon.com/acm/latest/APIReference/API_AddTagsToCertificate.html)  
* [x] [DescribeCertificateRequest](https://docs.aws.amazon.com/acm/latest/APIReference/API_DescribeCertificate.html)  
* [x] [ListTagsForCertificateRequest](https://docs.aws.amazon.com/acm/latest/APIReference/API_ListTagsForCertificate.html)  
* [x] [RequestCertificateRequest](https://docs.aws.amazon.com/acm/latest/APIReference/API_RequestCertificate.html)  

### Cognito-idp
* [x] [CreateResourceServerRequest](https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_CreateResourceServer.html)  
* [x] [CreateUserPoolClientRequest](https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_CreateUserPoolClient.html)  
* [x] [CreateUserPoolDomainRequest](https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_DescribeUserPoolDomain.html)  
* [x] [CreateUserPoolRequest](https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_CreateUserPool.html)  
* [x] [DeleteUserPoolRequest](https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_DeleteUserPool.html)  
* [x] [DescribeResourceServerRequest](https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_DescribeResourceServer.html)  
* [x] [DescribeUserPoolClientRequest](https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_DescribeUserPoolClient.html)  
* [x] [DescribeUserPoolDomainRequest](https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_DescribeUserPoolDomain.html)  
* [x] [DescribeUserPoolRequest](https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_DescribeUserPool.html)  
* [x] [UpdateUserPoolClientRequest](https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_UpdateUserPoolClient.html)  
* [x] [UpdateUserPoolRequest](https://docs.aws.amazon.com/cognito-user-identity-pools/latest/APIReference/API_UpdateUserPool.html)  

### Cognito User Pool Auth
* [x] [JWKS](https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-tokens-verifying-a-jwt.html)  
* [x] [Token Endpoint](https://docs.aws.amazon.com/cognito/latest/developerguide/token-endpoint.html)
- Supports only `client_credentials`  

### EC2
* [x] [AuthorizeSecurityGroupEgressRequest](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_AuthorizeSecurityGroupEgress.html)  
* [x] [CreateSecurityGroupRequest](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_CreateSecurityGroup.html)  
* [x] [CreateVpcEndpointRequest](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_CreateVpcEndpoint.html)  
* [x] [DescribeAccountAttributesRequest](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeAccountAttributes.html)  
* [x] [DescribePrefixListsRequest](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribePrefixLists.html)  
* [x] [DescribeSecurityGroupsRequest](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSecurityGroups.html)  
* [x] [DescribeVpcEndpointsRequest](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVpcEndpoints.html)  
* [x] [DescribeVpcEndpointServicesRequest](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVpcEndpointServices.html)  
* [x] [ModifyVpcEndpointRequest](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_ModifyVpcEndpoint.html)  

### KMS
* [x] [CreateAliasRequest](https://docs.aws.amazon.com/kms/latest/APIReference/API_CreateAlias.html)  
* [x] [CreateKeyRequest](https://docs.aws.amazon.com/kms/latest/APIReference/API_CreateKey.html)  
* [x] [DeleteAliasRequest](https://docs.aws.amazon.com/kms/latest/APIReference/API_DeleteAlias.html)  
* [x] [DescribeKeyRequest](https://docs.aws.amazon.com/kms/latest/APIReference/API_DescribeKey.html)  
* [x] [GetKeyPolicyRequest](https://docs.aws.amazon.com/kms/latest/APIReference/API_GetKeyPolicy.html)  
* [x] [GetKeyRotationStatusRequest](https://docs.aws.amazon.com/kms/latest/APIReference/API_GetKeyRotationStatus.html)  
* [x] [ListAliasesRequest](https://docs.aws.amazon.com/kms/latest/APIReference/API_ListAliases.html)  
* [x] [ListResourceTagsRequest](https://docs.aws.amazon.com/kms/latest/APIReference/API_ListResourceTags.html)  
* [x] [UpdateKeyDescriptionRequest](https://docs.aws.amazon.com/kms/latest/APIReference/API_UpdateKeyDescription.html)  

### Route53
* [x] [ChangeResourceRecordSetsRequest](https://docs.aws.amazon.com/Route53/latest/APIReference/API_ChangeResourceRecordSets.html)  
* [x] [ChangeTagsForResourceRequest](https://docs.aws.amazon.com/Route53/latest/APIReference/API_ChangeTagsForResource.html)  
* [x] [CreateHostedZoneRequest](https://docs.aws.amazon.com/Route53/latest/APIReference/API_CreateHostedZone.html)  
* [x] [ListHostedZonesRequest](https://docs.aws.amazon.com/Route53/latest/APIReference/API_ListHostedZones.html)  
* [x] [ListResourceRecordSetsRequest](https://docs.aws.amazon.com/Route53/latest/APIReference/API_ListResourceRecordSets.html)  
* [x] [ListTagsForResourceRequest](https://docs.aws.amazon.com/Route53/latest/APIReference/API_ListTagsForResource.html)  
* [x] [UpdateHostedZoneCommentRequest](https://docs.aws.amazon.com/Route53/latest/APIReference/API_UpdateHostedZoneComment.html)  
