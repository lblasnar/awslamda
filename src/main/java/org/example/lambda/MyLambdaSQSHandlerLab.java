package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.util.HashMap;

import static org.example.utils.LambdaUtils.initializeLogger;

/**
 * @author luigi
 * 20/02/2023
 */
public class MyLambdaSQSHandlerLab {
    public String handleRequest(SQSEvent input, Context context) {
        LambdaLogger logger = initializeLogger(context);
        logger.log("Received SQS event");
        logger.log("Amount of events: " + input.getRecords().size());
        var records = input.getRecords();
        var events = new HashMap<>();
        records.forEach(r -> {
            var messageId = r.getMessageId();
            logger.log("SQS Event id: " + messageId);
            var body = r.getBody();
            logger.log("SQS Body: " + body);
            events.put(messageId, body);
        });

        String messageToSend;
        if (!records.isEmpty()) {
            messageToSend = "SQS events id: " + events.keySet();
        } else {
            messageToSend = "No SQS events found";
        }
        return messageToSend;
    }
}
