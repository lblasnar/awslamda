package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.example.utils.LambdaUtils;

import java.io.Serializable;

/**
 * @author luigi
 * 17/02/2023
 */
public class MyJavaLambdaHandler implements RequestHandler<Serializable, String> {
    @Override
    public String handleRequest(Serializable input, Context context) {
        var logger = LambdaUtils.initializeLogger(context);
        String eventType;
        logger.log("Message received from type" + input.getClass());
        if (input instanceof SNSEvent) {
            eventType = new MyLambdaHandlerLab().handleRequest((SNSEvent) input, context);
        } else if (input instanceof KinesisEvent) {
            eventType = new MyLambdaKinesisHandlerLab().handleRequest((KinesisEvent) input, context);
        } else if (input instanceof SQSEvent) {
            eventType = new MyLambdaSQSHandlerLab().handleRequest((SQSEvent) input, context);
        } else {
            logger.log("Not known event type");
            eventType = "Not known event";
        }
        return eventType;
    }
}
