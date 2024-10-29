# Localstack

This is a demo project for using AWS service locally by `Localstack`.

## Table of Contents

- [Project Setup](#project-setup)

## Project Setup

To build and run the project, follow these steps:

* Clone the repository.
* Run `sudo docker-compose up -d` to install `Localstack`.
* Install `AWS-CLI` in your machine.
* Create a `DynamoDB Table` using command: <br>
  `awslocal dynamodb create-table \` <br>
  `--table-name shipment \` <br>
  `--key-schema AttributeName=id,KeyType=HASH \` <br>
  `--attribute-definitions AttributeName=id,AttributeType=S \` <br>
  `--billing-mode PAY_PER_REQUEST \` <br>
  `--region ap-south-1` <br>
  `Here, Table Name: 'shipment' & Primary Key: 'id'`
* Create an `S3 Bucket` using command: <br>
  `awslocal s3api create-bucket --bucket shipment-picture-bucket` <br>
  `Here, Bucket Name: shipment-picture-bucket`
* Run the Project.
