environment                                   = "Prod"
#SQS
sqs_name                                      = "SQS_Prod"
sqs_delay                                     = 90
sqs_max_message_size                          = 2048
sqs_message_retention_seconds                 = 86400
sqs_receive_wait_time_seconds                 = 10
#Lambda
lambda_runtime                                = "java11"
lambda_handler                                = "org.example.lambda.HandlerStream"
lambda_function_name                          = "Lab_lambda_function_Prod"
lambda_filename                               = "../target/LambdaTest-1.0-SNAPSHOT.jar"
lambda_memory_size                            = 1024
lambda_timeout                                = 20
lambda_publish                                = true
lambda_alias_name                             = "Prod"
lambda_alias_description                      = "Prod Environment"
lambda_alias_version                          = 4
lambda_event_source_mapping_enabled           = true
lambda_allow_invocation_from_sns_statement_id = "AllowExecutionFromSNSPR"
lambda_allow_invocation_from_sns_action       = "lambda:*"
lambda_allow_invocation_from_sns_principal    = "sns.amazonaws.com"
lambda_role_name                              = "LambdaRole_PROD"
lambda_max_concurrency                        = 10
#SNS
sns_protocol                                  = "lambda"
sns_name                                      = "SNS_Prod"
#CloudWatch
cloudwatch_alarm_period                       = 60
cloudwatch_alarm_evaluation_periods           = 3
cloudwatch_alarm_threshold                    = 0.8
alarm_email                                   = "luis.blas.narvaez@gmail.com"