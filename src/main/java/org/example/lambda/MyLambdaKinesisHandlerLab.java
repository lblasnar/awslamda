package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

import java.util.ArrayList;

import static org.example.utils.LambdaUtils.initializeLogger;

/**
 * @author luigi
 * 15/02/2023
 */
public class MyLambdaKinesisHandlerLab implements RequestHandler<KinesisEvent, String> {
    private LambdaLogger logger;

    @Override
    public String handleRequest(KinesisEvent input, Context context) {
        logger = initializeLogger(context);
        logger.log("Received Kinesis event");
        logger.log("Amount of events: " + input.getRecords().size());
        var records = input.getRecords();
        var events = new ArrayList<>();
        records.forEach(r -> {
            String eventName = r.getEventName();
            logger.log("Event name: " + eventName);
            logger.log("Kinesis: " + r.getKinesis().toString());
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
