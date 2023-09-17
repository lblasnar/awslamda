terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.67"
    }
    archive = {
      source  = "hashicorp/archive"
      version = "~> 2.4"
    }
  }
  required_version = ">= 1.2.0"
}

provider "aws" {
  region = "us-east-1"
}
data "archive_file" "source_code" {
  type        = "zip"
  source_file = var.lambda_filename
  output_path = "lambda_function.zip"
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
  filename         = var.lambda_filename
  function_name    = var.lambda_function_name
  role             = aws_iam_role.my_aws_role.arn
  handler          = var.lambda_handler
  runtime          = var.lambda_runtime
  timeout          = var.lambda_timeout
  memory_size      = var.lambda_memory_size
  source_code_hash = data.archive_file.source_code.output_base64sha256
  publish          = var.lambda_publish

  tracing_config {
    mode = "Active"
  }
}

#resource "aws_lambda_provisioned_concurrency_config" "my_function_provisioned_concurrency" {
#  function_name                     = var.lambda_function_name
#  provisioned_concurrent_executions = var.lambda_max_concurrency
#  qualifier                         = aws_lambda_alias.lambda_alias.name
#}

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
                  "oam:ListSinks",
                  "events:*"
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
########################################################################################################################
# CLOUDWATCH
########################################################################################################################
resource "aws_cloudwatch_metric_alarm" "emailAlarmErrors" {
  alarm_name          = "lambda-failure-alarm"
  alarm_description   = "This alarm alerts when a Lambda function fails consecutively."
  metric_name         = "Errors"
  namespace           = "AWS/Lambda"
  period              = var.cloudwatch_alarm_period
  evaluation_periods  = var.cloudwatch_alarm_evaluation_periods
  threshold           = var.cloudwatch_alarm_threshold
  statistic           = "SampleCount"
  comparison_operator = "GreaterThanThreshold"
  actions_enabled     = "true"
  alarm_actions       = [aws_sns_topic.alarm_topic.arn]
}
resource "aws_cloudwatch_metric_alarm" "emailAlarmConcurrentExec" {
  alarm_name          = "lambda-concurrent-execution-alarm"
  alarm_description   = "This alarm alerts when a Lambda function has more than a threshold concurrent executions."
  metric_name         = "ConcurrentExecutions"
  namespace           = "AWS/Lambda"
  period              = var.cloudwatch_alarm_period
  evaluation_periods  = var.cloudwatch_alarm_evaluation_periods
  threshold           = var.cloudwatch_alarm_threshold_concurrent_execution
  statistic           = "SampleCount"
  comparison_operator = "GreaterThanThreshold"
  actions_enabled     = "true"
  alarm_actions       = [aws_sns_topic.alarm_topic.arn]
}
# SNS topic to send emails with the Alerts
resource "aws_sns_topic" "alarm_topic" {
  name = "alarm-topic"

  ## This local exec, suscribes your email to the topic
  provisioner "local-exec" {
    command = "aws sns subscribe --topic-arn ${self.arn} --protocol email --notification-endpoint ${var.alarm_email} --region us-east-1"
  }
}
resource "aws_sns_topic_policy" "alarm_sns_policy" {
  arn    = aws_sns_topic.alarm_topic.arn
  policy = data.aws_iam_policy_document.sns_topic_policy.json
}

resource "aws_iam_role" "cloudwatch_alarms_role" {
  name               = "cloudwatch_alarms_role"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "events.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "cloudwatch_alarms_role_policy" {
  name   = "cloudwatch_alarms_role_policy"
  role   = aws_iam_role.cloudwatch_alarms_role.name
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": [
          "cloudwatch.amazonaws.com"
        ]
      },
      "Action": "sns:Publish",
      "Resource": "${aws_sns_topic.alarm_topic.arn}"
    }
  ]
}
EOF
}