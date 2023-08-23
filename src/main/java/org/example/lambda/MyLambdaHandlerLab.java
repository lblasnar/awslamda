package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.api.APIfz;
import org.example.api.RetroFitAPI;
import org.example.pojo.MessageDTO;
import org.example.pojo.disney.ClassificationDTO;
import org.joda.time.Instant;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author luigi
 * 15/02/2023
 */
public class MyLambdaHandlerLab implements RequestHandler<SNSEvent, String> {
    private static final Logger logger = LogManager.getLogger(MyLambdaHandlerLab.class);

    @Override
    public String handleRequest(SNSEvent input, Context context) {
        logger.info("Received SNS event");
        String dateReceived = String.format("Received event at : %s", Instant.now());
        logger.info(dateReceived);
        var records = input.getRecords();
        List<String> ids = new ArrayList<>();
        records.forEach(snsRecord -> {
            SNSEvent.SNS sns = snsRecord.getSNS();
            //From message get id
            var snsMessage = sns.getMessage();
            logger.info("SNS message: {}", snsMessage);
            logger.info("SNS message attributes: {}", sns.getMessageAttributes());
            logger.info("SNS type: {}", sns.getType());
            var message = Optional.ofNullable(new Gson().fromJson(snsMessage, MessageDTO.class));
            message.ifPresent(value -> {
                var id = value.getId();
                ids.add(id);
                logger.info(String.format("Id received: %s", id));
            });
        });
        ids.forEach(this::sendToRetroFit);

        String messageToSend;
        if (!ids.isEmpty()) {
            messageToSend = "The message was: " + ids;
        } else {
            messageToSend = "No ids found in the message";
        }
        return messageToSend;
    }

    private void sendToRetroFit(String id) {
        logger.info("Initiating retrofit");
        //Send to Retrofit
        var retroFitAPI = new RetroFitAPI();
        var retrofit = retroFitAPI.getRetrofit();
        var apIfz = retrofit.create(APIfz.class);
        var url = "/v3/kabc/item/" + id + "?key=otv.web.kabc.story";
        var retrofitURL = RetroFitAPI.BASE_URL + url;
        logger.info("Sending Retrofit to: {}", retrofitURL);
        Call<ClassificationDTO> call = apIfz.getId(url);
        Response<ClassificationDTO> response;
        try {
            response = call.execute();
            if (!response.isSuccessful()) {
                logger.info("ERROR: Something wrong with the response of Retrofit");
            }
        } catch (IOException e) {
            logger.info(String.format("ERROR: %s", e.getMessage()));
        }
    }
}
