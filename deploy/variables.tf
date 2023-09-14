variable "environment" {
  type        = string
  description = "The environment to deploy the changes"

  validation {
    condition     = contains(["QA", "Prod"], var.environment)
    error_message = "The environment value must be QA or Prod"
  }
}

########################################################################################################################
###SQS
########################################################################################################################
variable "sqs_name" {
  type        = string
  description = "The SQS name"
  default     = "SQS"
}

variable "lambda_role_name" {
  type        = string
  description = "The lambda role name"
  default     = "LambdaRole"
}

variable "sqs_delay" {
  type        = number
  description = "The time in seconds that the delivery of all messages in the queue will be delayed. An integer from 0 to 900 (15 minutes). The default for this attribute is 0 seconds."
  default     = 0
  validation {
    condition     = var.sqs_delay > 0 && var.sqs_delay < 900
    error_message = "Must be an integer from 0 to 900"
  }
}

variable "sqs_max_message_size" {
  type        = number
  description = "(Optional) The limit of how many bytes a message can contain before Amazon SQS rejects it. An integer from 1024 bytes (1 KiB) up to 262144 bytes (256 KiB)."
  default     = 262144
  validation {
    condition     = var.sqs_max_message_size > 1024 && var.sqs_max_message_size < 262144
    error_message = "Must be an integer from 1024 to 262144"
  }
}

variable "sqs_message_retention_seconds" {
  type        = number
  description = "(Optional) The number of seconds Amazon SQS retains a message. Integer representing seconds, from 60 (1 minute) to 1209600 (14 days)."
  default     = 262144
  validation {
    condition     = var.sqs_message_retention_seconds > 60 && var.sqs_message_retention_seconds < 1209600
    error_message = "Must be an integer from 60 to 1209600"
  }
}

variable "sqs_receive_wait_time_seconds" {
  type        = number
  description = "(Optional) The time for which a ReceiveMessage call will wait for a message to arrive (long polling) before returning. An integer from 0 to 20 (seconds). The default for this attribute is 0, meaning that the call will return immediately."
  default     = 0
  validation {
    condition     = var.sqs_receive_wait_time_seconds > 0 && var.sqs_receive_wait_time_seconds < 20
    error_message = "Must be an integer from 0 to 20"
  }
}

########################################################################################################################
###SNS
########################################################################################################################
variable "sns_name" {
  type        = string
  description = "The SNS name"
  default     = "SNS"
}

variable "sns_protocol" {
  type        = string
  description = "The SNS protocol"
  default     = "lambda"
}


########################################################################################################################
###Lambda
########################################################################################################################
variable "lambda_filename" {
  type        = string
  description = "The file name to upload, If the file is not in the current working directory you will need to include a path.module in the filename."
  default     = "../target/LambdaTest-1.0-SNAPSHOT.jar"
}

variable "lambda_function_name" {
  type        = string
  description = "The lambda function name"
  default     = "Lab_lambda_function"
}

variable "lambda_handler" {
  type        = string
  description = "The handler"
  default     = "org.example.lambda.HandlerStream"
}

variable "lambda_runtime" {
  type        = string
  description = "Runtime"
  default     = "java11"
}

variable "lambda_timeout" {
  type        = number
  description = "Timeout of the lambda in seconds"
  default     = 5
}

variable "lambda_memory_size" {
  type        = number
  description = "Lambda memory size"
  default     = 1024
}

variable "lambda_publish" {
  type        = bool
  description = "If has to publish"
  default     = true
}

variable "lambda_tracing_config" {
  type    = string
  default = "Active"
}

variable "lambda_alias_name" {
  type    = string
  default = "Alias"
}

variable "lambda_alias_description" {
  type    = string
  default = "Alias description"
}

variable "lambda_alias_version" {
  type        = number
  description = "Version of lambda"
}

variable "lambda_event_source_mapping_enabled" {
  type    = bool
  default = true
}

variable "lambda_allow_invocation_from_sns_statement_id" {
  type    = string
  default = "AllowExecutionFromSNSPR"
}

variable "lambda_allow_invocation_from_sns_action" {
  type    = string
  default = "lambda:*"
}

variable "lambda_allow_invocation_from_sns_principal" {
  type    = string
  default = "sns.amazonaws.com"
}

