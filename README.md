# Localstack

This is a demo project for using AWS service locally by `Localstack`.

## Table of Contents

- [Project Setup](#project-setup)

## Project Setup

To build and run the project, follow these steps:

* Clone the repository.
* Run `sudo docker-compose up -d` to install `Localstack`.
* Install `AWS-CLI` in your machine.
* Run the `aws-setup.sh` file inside `/src/main/resources/aws` to create `s3-bucket`, `sqs-queue` & `dynamodb table`.
* Run the Project.
