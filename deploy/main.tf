terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }
  required_version = ">= 1.2.0"
}

provider "aws" {
  region = "us-east-1"
}

#################################################################################################
# SQS
resource "aws_sqs_queue" "my_terraform_sqs_queue" {
  name                      = var.sqs_name
  delay_seconds             = var.sqs_delay
  max_message_size          = var.sqs_max_message_size
  message_retention_seconds = var.sqs_message_retention_seconds
  receive_wait_time_seconds = var.sqs_receive_wait_time_seconds

  tags = {
    Environment = var.environment
  }
}

#SNS
resource "aws_sns_topic" "my_terraform_sns_topic" {
  name = var.sns_name
  tags = {
    Environment = var.environment
  }
}

resource "aws_sns_topic_policy" "my_sns_policy" {
  arn    = aws_sns_topic.my_terraform_sns_topic.arn
  policy = data.aws_iam_policy_document.sns_topic_policy.json
}

data "aws_iam_policy_document" "sns_topic_policy" {
  statement {
    actions = [
      "SNS:Subscribe",
      "SNS:Receive"
    ]
    effect = "Allow"
    principals {
      type        = "AWS"
      identifiers = ["*"]
    }
    resources = [
      aws_sns_topic.my_terraform_sns_topic.arn,
    ]
  }
}

# Lambda
resource "aws_lambda_function" "lambda_function" {
  filename      = var.lambda_filename
  function_name = var.lambda_function_name
  role          = aws_iam_role.my_aws_role.arn
  handler       = var.lambda_handler
  runtime       = var.lambda_runtime
  timeout       = 5
  memory_size   = 1024
  publish       = true

  tracing_config {
    mode = "Active"
  }
}
resource "aws_iam_role" "my_aws_role" {
  name               = var.lambda_role_name
  assume_role_policy = <<EOF
  {
    "Version": "2012-10-17",
    "Statement": [
      {
          "Action": "sts:AssumeRole",
          "Effect": "Allow",
          "Principal": {
              "Service": "lambda.amazonaws.com"
          }
      }
    ]
  }
  EOF
  tags               = {
    Environment = var.environment
  }
}

resource "aws_iam_role_policy" "lambda_role_sqs_policy" {
  name   = "AllowSQSPermissions"
  role   = aws_iam_role.my_aws_role.id
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "sqs:*"
            ],
            "Effect": "Allow",
            "Resource": "*"
        }
    ]
}
  EOF
}

resource "aws_iam_role_policy" "lambda_role_cloudwatch_policy" {
  name   = "AllowCloudWatchPermissions"
  role   = aws_iam_role.my_aws_role.id
  policy = <<EOF
{
      "Version": "2012-10-17",
      "Statement": [
          {
              "Effect": "Allow",
              "Action": [
                  "autoscaling:Describe*",
                  "cloudwatch:*",
                  "logs:*",
                  "sns:*",
                  "iam:GetPolicy",
                  "iam:GetPolicyVersion",
                  "iam:GetRole",
                  "oam:ListSinks"
              ],
              "Resource": "*"
          },
          {
              "Effect": "Allow",
              "Action": "iam:CreateServiceLinkedRole",
              "Resource": "arn:aws:iam::*:role/aws-service-role/events.amazonaws.com/AWSServiceRoleForCloudWatchEvents*",
              "Condition": {
                  "StringLike": {
                      "iam:AWSServiceName": "events.amazonaws.com"
                  }
              }
          },
          {
              "Effect": "Allow",
              "Action": [
                  "oam:ListAttachedLinks"
              ],
              "Resource": "arn:aws:oam:*:*:sink/*"
          }
      ]
  }
  EOF
}

resource "aws_iam_role_policy" "lambda_role_logs_policy" {
  name   = "LambdaCloudWatchLogsPolicy"
  role   = aws_iam_role.my_aws_role.id
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Action": [
          "logs:*"
        ],
        "Effect": "Allow",
        "Resource": "*"
      }
    ]
  }
  EOF
}

resource "aws_iam_role_policy" "lambda_role_sns_policy" {
  name   = "AllowSNSPermissions"
  role   = aws_iam_role.my_aws_role.id
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Action": [
          "SNS:*"
        ],
        "Effect": "Allow",
        "Resource": "*"
      }
    ]
  }
EOF
}

resource "aws_iam_role_policy" "lambda_role_xray_policy" {
  name   = "AllowXRayPermissions"
  role   = aws_iam_role.my_aws_role.id
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "xray:*"
            ],
            "Resource": [
                "*"
            ]
        }
    ]
  }
EOF
}

# Event source from SQS
resource "aws_lambda_event_source_mapping" "event_source_mapping_sqs" {
  event_source_arn = aws_sqs_queue.my_terraform_sqs_queue.arn
  enabled          = var.lambda_event_source_mapping_enabled
  function_name    = aws_lambda_alias.lambda_alias.arn
  batch_size       = 1
}
# Event source from SNS
resource "aws_sns_topic_subscription" "event_subscription" {
  endpoint  = aws_lambda_alias.lambda_alias.arn
  protocol  = var.sns_protocol
  topic_arn = aws_sns_topic.my_terraform_sns_topic.arn
}

resource "aws_lambda_permission" "allow_invocation_from_sns" {
  statement_id  = var.lambda_allow_invocation_from_sns_statement_id
  action        = var.lambda_allow_invocation_from_sns_action
  function_name = aws_lambda_function.lambda_function.function_name
  principal     = var.lambda_allow_invocation_from_sns_principal
  source_arn    = aws_sns_topic.my_terraform_sns_topic.arn
  qualifier     = aws_lambda_alias.lambda_alias.name
}

resource "aws_lambda_alias" "lambda_alias" {
  name             = var.lambda_alias_name
  description      = var.lambda_alias_description
  function_name    = aws_lambda_function.lambda_function.function_name
  function_version = var.lambda_alias_version
}