terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }
  required_version = ">= 1.2.0"
}

# terraform apply -var="env=The_Value"
variable "env" {
  description = "Environment to deploy"
  type        = string
  default     = "QA"
}

provider "aws" {
  region = "us-east-1"
}

# SQS
resource "aws_sqs_queue" "my_terraform_sqs_queue" {
  name                      = "my_terraform_sqs_queue_${var.env}"
  # (Optional) The time in seconds that the delivery of all messages in the queue will be delayed. An integer from 0
  # to 900 (15 minutes). The default for this attribute is 0 seconds.
  delay_seconds             = 90
  # (Optional) The limit of how many bytes a message can contain before Amazon SQS rejects it. An integer from 1024
  #  bytes (1 KiB) up to 262144 bytes (256 KiB). The default for this attribute is 262144 (256 KiB).
  max_message_size          = 2048
  # (Optional) The number of seconds Amazon SQS retains a message. Integer representing seconds, from 60 (1 minute) to
  # 1209600 (14 days). The default for this attribute is 345600 (4 days).
  message_retention_seconds = 86400
  # (Optional) The time for which a ReceiveMessage call will wait for a message to arrive (long polling) before
  #  returning. An integer from 0 to 20 (seconds). The default for this attribute is 0, meaning that the call will
  #  return immediately.
  receive_wait_time_seconds = 10
  # (Optional) The JSON policy to set up the Dead Letter Queue, see AWS docs. Note: when specifying maxReceiveCount,
  # you must specify it as an integer (5), and not a string ("5").
  ############################################################
  # FOR NOW ONLY TO KNOW THAT EXIST
  ############################################################
  #  redrive_policy            = jsonencode({
  #    deadLetterTargetArn = aws_sqs_queue.terraform_queue_deadletter.arn
  #    # The maxReceiveCount is the number of times a consumer tries receiving a message from a queue without deleting it
  #    # before being moved to the dead-letter queue.
  #    maxReceiveCount = 4
  #  })

  tags = {
    Environment = var.env
  }
}

#SNS
resource "aws_sns_topic" "my_terraform_sns_topic" {
  name = "my_terraform_sns_${var.env}"
  tags = {
    Environment = var.env
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
resource "aws_lambda_function" "my_lambda_function" {
  # If the file is not in the current working directory you will need to include a
  # path.module in the filename.
  filename      = "../target/LambdaTest-1.0-SNAPSHOT.jar"
  function_name = "my_lambda_function_${var.env}"
  role          = aws_iam_role.my_aws_role.arn
  handler       = "index.test"
  runtime       = "java11"
  environment {
    variables = {
      QA   = "qa.api.abcotvs.com"
      Prod = "api.abcotvs.com"
    }
  }
  tags = {
    Environment = var.env
  }
}
resource "aws_iam_role" "my_aws_role" {
  name               = "LambdaRole"
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
    Environment = var.env
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
resource "aws_lambda_permission" "allow_invocation_from_sns" {
  statement_id  = "AllowExecutionFromSNS"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.my_lambda_function.function_name
  principal     = "sns.amazonaws.com"
  source_arn    = aws_sns_topic.my_terraform_sns_topic.arn
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
  enabled          = true
  function_name    = aws_lambda_function.my_lambda_function.arn
  batch_size       = 1
}
# Event source from SNS
resource "aws_sns_topic_subscription" "event_subscription" {
  endpoint  = aws_lambda_function.my_lambda_function.arn
  protocol  = "lambda"
  topic_arn = aws_sns_topic.my_terraform_sns_topic.arn
}