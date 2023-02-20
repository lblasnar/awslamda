package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
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
    void happy_path_kinesis() {
        //Given
        given(kinesisEvent.getRecords()).willReturn(List.of(kinesisEventRecord));
        given(kinesisEventRecord.getKinesis()).willReturn(kinesis);
        given(kinesisEventRecord.getEventName()).willReturn("My kinesis event name");
        mockKinesis();
        given(context.getLogger()).willReturn(null);
        //When
        var myLambdaHandlerLabTest = new MyJavaLambdaHandler();
        var handleRequest = myLambdaHandlerLabTest.handleRequest(kinesisEvent, context);
        //Then
        Assertions.assertEquals("Kinesis events: " + List.of("My kinesis event name"), handleRequest);
    }

    @Test
    void happy_path_sqs() {
        //Given
        given(sqsEvent.getRecords()).willReturn(List.of(sqsMessage));
        given(sqsMessage.getBody()).willReturn("Test from sqs body");
        given(sqsMessage.getMessageId()).willReturn("741852");
        given(context.getLogger()).willReturn(null);
        //When
        var myLambdaHandlerLabTest = new MyJavaLambdaHandler();
        var handleRequest = myLambdaHandlerLabTest.handleRequest(sqsEvent, context);
        //Then
        Assertions.assertEquals("SQS events id: [741852]", handleRequest);
    }

    @Test
    void happy_path_sns() {
        //Given
        given(snsEvent.getRecords()).willReturn(List.of(snsRecord));
        given(snsRecord.getSNS()).willReturn(sns);
        given(context.getLogger()).willReturn(null);
        var json = "{\"id\":\"2662585\",\"description\":\"Test Payload\"}";
        //When
        when(sns.getMessage()).thenReturn(json);
        var myLambdaHandlerLabTest = new MyLambdaHandlerLab();
        var handleRequest = myLambdaHandlerLabTest.handleRequest(snsEvent, context);
        //Then
        Assertions.assertEquals("The message was: " + List.of("2662585"), handleRequest);
    }

    private void mockKinesis() {
        given(kinesis.getKinesisSchemaVersion()).willReturn("1");
        given(kinesis.getApproximateArrivalTimestamp()).willReturn(Date.from(Instant.now()));
        given(kinesis.getSequenceNumber()).willReturn("456789");
        given(kinesis.getEncryptionType()).willReturn("Test");
        given(kinesis.getPartitionKey()).willReturn("Key");
        var bytes = new byte[10];
        var buffer = ByteBuffer.wrap(bytes);
        given(kinesis.getData()).willReturn(buffer);
    }
}