package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * @author luigi
 * 15/02/2023
 */
public class MyLambdaKinesisHandlerLab implements RequestHandler<KinesisEvent, String> {
    private static final Logger logger = LogManager.getLogger(MyLambdaKinesisHandlerLab.class);

    @Override
    public String handleRequest(KinesisEvent input, Context context) {
        logger.info("Received Kinesis event");
        logger.info("Amount of events: " + input.getRecords().size());
        var records = input.getRecords();
        var events = new ArrayList<>();
        records.forEach(r -> {
            String eventName = r.getEventName();
            logger.info("Event name: " + eventName);
            logger.info("Kinesis: " + r.getKinesis().toString());
            events.add(eventName);
        });

        String messageToSend;
        if (!records.isEmpty()) {
            messageToSend = "Kinesis events: " + events;
        } else {
            messageToSend = "No kinesis events found";
        }
        return messageToSend;
    }
}
