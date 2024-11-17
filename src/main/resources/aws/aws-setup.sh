#!/bin/bash

export LOCALSTACK_ENDPOINT_URL=http://localhost:4566
export SHIPMENT_QUEUE=shipment-sqs-queue.fifo
export SHIPMENT_PICTURE_BUCKET=shipment-picture-bucket
export SHIPMENT_DYNAMO_TABLE=shipment
export SHIPMENT_TABLE_KEY=id

echo "### Creating Shipment SQS Queue"
aws --endpoint-url=$LOCALSTACK_ENDPOINT_URL sqs create-queue --queue-name $SHIPMENT_QUEUE \
     --attributes '{
                   "VisibilityTimeout": "20",
                   "FifoQueue": "true"
                   }'

echo "### Creating Shipment S3 Bucket"
aws --endpoint-url=$LOCALSTACK_ENDPOINT_URL s3api create-bucket --bucket $SHIPMENT_PICTURE_BUCKET

echo "### Creating Shipment Dynamodb Table"
aws --endpoint-url=$LOCALSTACK_ENDPOINT_URL dynamodb create-table \
    --table-name $SHIPMENT_DYNAMO_TABLE \
    --key-schema AttributeName=$SHIPMENT_TABLE_KEY,KeyType=HASH \
    --attribute-definitions AttributeName=$SHIPMENT_TABLE_KEY,AttributeType=S \
    --billing-mode PAY_PER_REQUEST \
    --region ap-southeast-1
