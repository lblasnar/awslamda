package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author luigi
 * 16/02/2023
 */
class MyLambdaHandlerLabTest {

    final SNSEvent snsEvent = mock(SNSEvent.class);

    final SNSEvent.SNSRecord snsRecord = mock(SNSEvent.SNSRecord.class);

    final SNSEvent.SNS sns = mock(SNSEvent.SNS.class);

    @Mock
    final Context context = mock(Context.class);

    @Test
    void happy_path() {
        //Given
        given(snsEvent.getRecords()).willReturn(List.of(snsRecord));
        given(snsRecord.getSNS()).willReturn(sns);
        given(context.getLogger()).willReturn(null);
        String json = "{\"id\":\"2662585\",\"description\":\"Test Payload\"}";
        //When
        when(sns.getMessage()).thenReturn(json);
        var myLambdaHandlerLabTest = new MyLambdaHandlerLab();
        String handleRequest = myLambdaHandlerLabTest.handleRequest(snsEvent, context);
        //Then
        Assertions.assertEquals("The message was: " + List.of("2662585"), handleRequest);
    }

    @Test
    void wrong_path() {
        //Given
        given(snsEvent.getRecords()).willReturn(List.of(snsRecord));
        given(snsRecord.getSNS()).willReturn(sns);
        given(context.getLogger()).willReturn(null);
        String json = "";
        //When
        when(sns.getMessage()).thenReturn(json);
        //Then
        var myLambdaHandlerLabTest = new MyLambdaHandlerLab();
        String handleRequest = myLambdaHandlerLabTest.handleRequest(snsEvent, context);
        Assertions.assertEquals("No ids found in the message", handleRequest);
    }
}