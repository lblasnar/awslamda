package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * @author luigi
 * 13/02/2023
 */
public class MyTestLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var pathParameters = input.getPathParameters();
        var userId = pathParameters.get("userId");

        JsonObject json = new JsonObject();
        json.addProperty("name", "Luis");
        json.addProperty("lastName", "Blas");
        json.addProperty("id", userId);
        var responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.withBody(json.toString())
                .withStatusCode(200)
                .withHeaders(Map.of("Test", "test"));
        return responseEvent;
    }
}
