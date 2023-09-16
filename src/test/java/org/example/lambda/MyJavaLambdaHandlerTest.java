package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.example.exceptions.MyHandlerException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author luigi
 * 17/02/2023
 */
class MyJavaLambdaHandlerTest {

    final SNSEvent snsEvent = mock(SNSEvent.class);
    final SNSEvent.SNSRecord snsRecord = mock(SNSEvent.SNSRecord.class);
    final SNSEvent.SNS sns = mock(SNSEvent.SNS.class);
    final Context context = mock(Context.class);
    final KinesisEvent kinesisEvent = mock(KinesisEvent.class);
    final KinesisEvent.KinesisEventRecord kinesisEventRecord = mock(KinesisEvent.KinesisEventRecord.class);
    final KinesisEvent.Record kinesis = mock(KinesisEvent.Record.class);
    final SQSEvent sqsEvent = mock(SQSEvent.class);
    final SQSEvent.SQSMessage sqsMessage = mock(SQSEvent.SQSMessage.class);

    @Test
    void sqs_happy_path() {
        //Given
        var message = getSQSMessage();
        var inputStream = new ByteArrayInputStream(message.getBytes());
        //When
        var myLambdaHandlerLabTest = new HandlerStream();
        //Then
        assertDoesNotThrow(getExecutable(myLambdaHandlerLabTest, inputStream));
        assertEquals(1, myLambdaHandlerLabTest.getReceivedEvents().size());
        assertEquals(getExpectedSQS().getBody(),
                ((SQSEvent.SQSMessage) myLambdaHandlerLabTest.getReceivedEvents().get(0)).getBody());
        assertEquals(SQSEvent.SQSMessage.class, myLambdaHandlerLabTest.getReceivedEvents().get(0).getClass());
    }

    @Test
    void sns_happy_path() {
        //Given
        var message = getSNSMessage();
        var inputStream = new ByteArrayInputStream(message.getBytes());
        when(context.getInvokedFunctionArn())
                .thenReturn("arn:aws:lambda:us-east-1:350407421116:function:Lab_lambda_function_QA:QA");
        when(context.getFunctionName()).thenReturn("Lab_lambda_function_QA ");
        //When
        var myLambdaHandlerLabTest = new HandlerStream();
        //Then
        assertDoesNotThrow(getExecutable(myLambdaHandlerLabTest, inputStream));
        assertEquals(1, myLambdaHandlerLabTest.getReceivedEvents().size());
        assertEquals(getExpectedSNS().getMessage(),
                ((SNSEvent.SNS) myLambdaHandlerLabTest.getReceivedEvents().get(0)).getMessage());
        assertEquals(SNSEvent.SNS.class, myLambdaHandlerLabTest.getReceivedEvents().get(0).getClass());
    }

    @Test
    void sns_unHappy_path() {
        //Given
        var message = "{\"id\":\"2662585\",\"description\":\"Test Payload\"}";
        var inputStream = new ByteArrayInputStream(message.getBytes());
        //When
        var myLambdaHandlerLabTest = new HandlerStream();
        //Then
        assertThrows(MyHandlerException.class, getExecutable(myLambdaHandlerLabTest, inputStream));
    }

    private Executable getExecutable(HandlerStream myLambdaHandlerLabTest, ByteArrayInputStream inputStream) {
        return () -> myLambdaHandlerLabTest.handleRequest(inputStream, new ByteArrayOutputStream(), context);
    }

    private SNSEvent.SNS getExpectedSNS() {
        var sns = new SNSEvent.SNS();
        sns.setMessage("{\"id\":\"2662585\",\"description\":\"Test Payload\"}");
        return sns;
    }

    private static SQSEvent.SQSMessage getExpectedSQS() {
        var message = new SQSEvent.SQSMessage();
        message.setBody("New SQS message generated with default no formatted body");
        message.setReceiptHandle("AQEBJhnNJcTDYd/gXwirHW86bdK8CbrWF8Ck+ee0oWjSd+FggE4k/BC1CYaMBGW2/XtMxKfHxGOjkku4yEbAGUxri1BGOjxPQkkac+Qh8saALF08zzsgJ2nOtbDozIVr85hsd9ZAzN4ztljFNRYemJm5Iug0VJciIt7DiEtK0fWqQ3F3mnnxZmZAeIn7W9VJHZT9x+P7mAg2l8O48v2a7LE5DOeAv+0PDwtlPi80mJdsmpgGDocsgagmObgYY68vbgeEk5aSKmXUfspyIbpW36zLAI9DsMNvdQnhWxcTk1zMZAcohCJMKYIck2ddKjb0RBJYcfYm3FNk+ge179DHkhxjjU0QWN15i9BjcDVsIVvB9DbcsK/WWW9yt6EVHrXkLFFV");
        message.setAttributes(Map.of("ApproximateFirstReceiveTimestamp", "1677075125936", "SentTimestamp", "1677075125917", "SenderId", "AIDAVDFPI5S6ODTSJYMUX", "ApproximateReceiveCount", "1"));
        message.setMd5OfMessageAttributes("19448236650b116a67d4ec357db0a537");
        message.setMessageId("76850fc3-a29f-4647-9fdf-84274052d048");
        message.setMd5OfBody("429b460e742f9cc0531e2050a8d148ef");
        message.setEventSource("aws:sqs");
        message.setEventSourceArn("arn:aws:sqs:us-east-1:350407421116:MyTestQueue");
        message.setAwsRegion("us-east-1");
        var mA1 = new SQSEvent.MessageAttribute();
        mA1.setDataType("String");
        var stringValue = "c1ef6193-4059-d441-4718-c0e959e7756d";
        mA1.setStringValue(stringValue);
        mA1.setBinaryValue(ByteBuffer.wrap(stringValue.getBytes()));
        mA1.setBinaryListValues(List.of());
        mA1.setStringListValues(List.of());
        var mA2 = new SQSEvent.MessageAttribute();
        mA2.setDataType("String");
        var stringValue2 = "text/plain;charset=UTF-8";
        mA2.setStringValue(stringValue2);
        mA2.setBinaryValue(ByteBuffer.wrap(stringValue2.getBytes()));
        mA2.setBinaryListValues(List.of());
        mA2.setStringListValues(List.of());
        var mA3 = new SQSEvent.MessageAttribute();
        mA3.setDataType("String");
        var stringValue3 = "1677075126442";
        mA3.setStringValue(stringValue3);
        mA2.setBinaryValue(ByteBuffer.wrap(stringValue2.getBytes()));
        mA2.setBinaryListValues(List.of());
        mA2.setStringListValues(List.of());
        message.setMessageAttributes(Map.of("timestamp", mA3, "contentType", mA2, "id", mA1));
        return message;
    }


    private static String getSQSMessage() {
        return "{\n" +
                "    \"Records\": [\n" +
                "        {\n" +
                "            \"messageId\": \"76850fc3-a29f-4647-9fdf-84274052d048\",\n" +
                "            \"receiptHandle\": \"AQEBJhnNJcTDYd/gXwirHW86bdK8CbrWF8Ck+ee0oWjSd+FggE4k/BC1CYaMBGW2/XtMxKfHxGOjkku4yEbAGUxri1BGOjxPQkkac+Qh8saALF08zzsgJ2nOtbDozIVr85hsd9ZAzN4ztljFNRYemJm5Iug0VJciIt7DiEtK0fWqQ3F3mnnxZmZAeIn7W9VJHZT9x+P7mAg2l8O48v2a7LE5DOeAv+0PDwtlPi80mJdsmpgGDocsgagmObgYY68vbgeEk5aSKmXUfspyIbpW36zLAI9DsMNvdQnhWxcTk1zMZAcohCJMKYIck2ddKjb0RBJYcfYm3FNk+ge179DHkhxjjU0QWN15i9BjcDVsIVvB9DbcsK/WWW9yt6EVHrXkLFFV\",\n" +
                "            \"body\": \"New SQS message generated with default no formatted body\",\n" +
                "            \"attributes\": {\n" +
                "                \"ApproximateReceiveCount\": \"1\",\n" +
                "                \"SentTimestamp\": \"1677075125917\",\n" +
                "                \"SenderId\": \"AIDAVDFPI5S6ODTSJYMUX\",\n" +
                "                \"ApproximateFirstReceiveTimestamp\": \"1677075125936\"\n" +
                "            },\n" +
                "            \"messageAttributes\": {\n" +
                "                \"id\": {\n" +
                "                    \"stringValue\": \"c1ef6193-4059-d441-4718-c0e959e7756d\",\n" +
                "                    \"stringListValues\": [],\n" +
                "                    \"binaryListValues\": [],\n" +
                "                    \"dataType\": \"String\"\n" +
                "                },\n" +
                "                \"contentType\": {\n" +
                "                    \"stringValue\": \"text/plain;charset=UTF-8\",\n" +
                "                    \"stringListValues\": [],\n" +
                "                    \"binaryListValues\": [],\n" +
                "                    \"dataType\": \"String\"\n" +
                "                },\n" +
                "                \"timestamp\": {\n" +
                "                    \"stringValue\": \"1677075126442\",\n" +
                "                    \"stringListValues\": [],\n" +
                "                    \"binaryListValues\": [],\n" +
                "                    \"dataType\": \"Number.java.lang.Long\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"md5OfMessageAttributes\": \"19448236650b116a67d4ec357db0a537\",\n" +
                "            \"md5OfBody\": \"429b460e742f9cc0531e2050a8d148ef\",\n" +
                "            \"eventSource\": \"aws:sqs\",\n" +
                "            \"eventSourceARN\": \"arn:aws:sqs:us-east-1:350407421116:MyTestQueue\",\n" +
                "            \"awsRegion\": \"us-east-1\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    private static String getSNSMessage() {
        return "{\n" +
                "    \"Records\": [\n" +
                "        {\n" +
                "            \"EventSource\": \"aws:sns\",\n" +
                "            \"EventVersion\": \"1.0\",\n" +
                "            \"EventSubscriptionArn\": \"arn:aws:sns:us-east-1:350407421116:test1:a9f97e84-b3ce-4535-b7cc-d10bcaca27a8\",\n" +
                "            \"Sns\": {\n" +
                "                \"Type\": \"Notification\",\n" +
                "                \"MessageId\": \"efd14834-ec6a-5fb6-8ac9-1444c91a7bb8\",\n" +
                "                \"TopicArn\": \"arn:aws:sns:us-east-1:350407421116:test1\",\n" +
                "                \"Subject\": \"purchase-transaction-subject\",\n" +
                "                \"Message\": \"{\\\"id\\\":\\\"2662585\\\",\\\"description\\\":\\\"Test Payload\\\"}\",\n" +
                "                \"Timestamp\": \"2023-02-22T14:14:08.015Z\",\n" +
                "                \"SignatureVersion\": \"1\",\n" +
                "                \"Signature\": \"BmYjywJSxqGfpKzwKfgmvkY/5NvisVDHgWnMPNndBluF81NMMnTGWKPi8EDs4q+owz+bPVN8n2I2V5uNK+5eZCvh0SFj1JjkUUzwxl7YXfjvCkIpPDjYLkRTRgSb6gN1BKIbtuGXoj4cZD+p81YRmUN5fGW1oqPyrHpi5kNOqMA4LZrR55bjU5SQn/f1gk/G+WDmUz5lR5R5vfBCSFPCnSdeyyzadJs8JUinmMxHXun8v/cWw68IweBJRpoasiXUqRqBsEqdSKcsujwIe83TGJFTXaO6M8YQg32NYnDKXNNR6QWtjRQu+wZiz1JbjPYuHXqnmyVIR1f57UqlFSEZLw==\",\n" +
                "                \"SigningCertUrl\": \"https://sns.us-east-1.amazonaws.com/SimpleNotificationService-56e67fcb41f6fec09b0196692625d385.pem\",\n" +
                "                \"UnsubscribeUrl\": \"https://sns.us-east-1.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:us-east-1:350407421116:test1:a9f97e84-b3ce-4535-b7cc-d10bcaca27a8\",\n" +
                "                \"MessageAttributes\": {\n" +
                "                    \"NOTIFICATION_SUBJECT_HEADER\": {\n" +
                "                        \"Type\": \"String\",\n" +
                "                        \"Value\": \"purchase-transaction-subject\"\n" +
                "                    },\n" +
                "                    \"id\": {\n" +
                "                        \"Type\": \"String\",\n" +
                "                        \"Value\": \"58026d1d-c266-3590-528c-4089e3440ace\"\n" +
                "                    },\n" +
                "                    \"contentType\": {\n" +
                "                        \"Type\": \"String\",\n" +
                "                        \"Value\": \"application/json\"\n" +
                "                    },\n" +
                "                    \"timestamp\": {\n" +
                "                        \"Type\": \"String\",\n" +
                "                        \"Value\": \"1677075248728\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    @Test
    void testSubstring() {
        var testString = "arn:aws:lambda:us-east-1:350407421116:function:Lab_lambda_function_1:QA";
        var env = testString.trim().substring(testString.lastIndexOf(":") + 1);
        assertEquals("QA", env);
    }
}