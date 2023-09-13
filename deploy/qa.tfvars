environment                                   = "QA"
#SQS
sqs_name                                      = "SQS_QA"
sqs_delay                                     = 90
sqs_max_message_size                          = 2048
sqs_message_retention_seconds                 = 86400
sqs_receive_wait_time_seconds                 = 10
#Lambda
lambda_runtime                                = "java11"
lambda_handler                                = "org.example.lambda.HandlerStream"
lambda_function_name                          = "Lab_lambda_function_QA"
lambda_filename                               = "../target/LambdaTest-1.0-SNAPSHOT.jar"
lambda_memory_size                            = 1024
lambda_timeout                                = 20
lambda_publish                                = true
lambda_alias_name                             = "QA"
lambda_alias_description                      = "QA Environment"
lambda_alias_version                          = 9
lambda_event_source_mapping_enabled           = true
lambda_allow_invocation_from_sns_statement_id = "AllowExecutionFromSNSPR"
lambda_allow_invocation_from_sns_action       = "lambda:*"
lambda_allow_invocation_from_sns_principal    = "sns.amazonaws.com"
lambda_role_name                              = "LambdaRole_QA"
#SNS
sns_protocol                                  = "lambda"
sns_name                                      = "SNS_QA"