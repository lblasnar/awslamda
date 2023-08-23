package org.example.lambda;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * @author luigi
 * 20/02/2023
 */
public class MyLambdaSQSHandlerLab {
    private static final Logger logger = LogManager.getLogger(MyLambdaSQSHandlerLab.class);


    public String handleRequest(SQSEvent input) {
        logger.info("Received SQS event");
        logger.info("Amount of events: {}", input.getRecords().size());
        var records = input.getRecords();
        var events = new HashMap<>();
        records.forEach(r -> {
            var messageId = r.getMessageId();
            logger.info("SQS Event id: {}", messageId);
            var body = r.getBody();
            logger.info("SQS Body: {}", body);
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
