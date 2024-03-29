package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.strategy.sampling.DefaultSamplingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.api.APIfz;
import org.example.api.RetroFitAPI;
import org.example.exceptions.MyHandlerException;
import org.example.pojo.disney.ClassificationDTO;
import org.joda.time.DateTime;
import retrofit2.Call;
import retrofit2.Response;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.api.RetroFitAPI.getBaseUrl;

public class HandlerStream implements RequestStreamHandler {
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = LogManager.getLogger(HandlerStream.class);
    @Getter
    final List<Object> receivedEvents = new ArrayList<>();

    private Integer aInteger = 0;
    private Context context;

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {
        this.context = context;
        aInteger++;
        initializeAWSXRay();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))) {
            logger.info("aInteger value: {}", aInteger);
            HashMap event = gson.fromJson(reader, HashMap.class);
            logger.info("STREAM TYPE: {}", inputStream.getClass());
            logger.info("EVENT TYPE: {}", event.getClass());
            logger.info("EVENT: {}", event);
            event.forEach((k, v) -> {
                logger.info(k.toString());
                logger.info("Key class {}", k.getClass());
                logger.info("Value class: {}", v.getClass());
                List<LinkedTreeMap> list = (List) v;
                logger.info("Size list: {}", list.size());
                logger.info("Class : {}", ((List<?>) v).get(0).getClass());
                filterByEventType(receivedEvents, list);
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new MyHandlerException(e);
        }
    }

    private void initializeAWSXRay() {
        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard();
        builder.withSamplingStrategy(new DefaultSamplingStrategy());
        AWSXRay.setGlobalRecorder(builder.build());
    }

    private void filterByEventType(List<Object> receivedEvents, List<LinkedTreeMap> list) {
        //Vemos cada uno de los mensajes que vienen
        list.forEach(value -> {
            logger.info("A value of list : {}", value);
            logger.info("Class : {}", value.getClass());
            var eventSource = value.keySet().stream()
                    .filter(key -> key.toString().equalsIgnoreCase("eventsource"))
                    .findFirst();
            eventSource.ifPresent(aEvent -> {
                var theEvent = value.get(aEvent);
                logger.info("EventSource: {}", theEvent);
                logger.info("EventSource class: {}", theEvent.getClass());
                String eventStr = theEvent.toString();
                if (eventStr.contains("sns")) {
                    logger.info("IT'S A SNS!!!");
                    LinkedTreeMap sns = (LinkedTreeMap) value.get("Sns");
                    logger.info("Value SNS : {}", sns);
                    logger.info("Class SNS : {}", sns.getClass());
                    logger.info("Parsing to SNSEvent");
                    var snsMessage = parseSNSMessage(sns);
                    JsonObject jsonObject = new Gson().fromJson(snsMessage.getMessage(), JsonObject.class);
                    String id = jsonObject.getAsJsonPrimitive("id").getAsString();
                    logger.info("Id to Retrofit: {}", id);
                    sendToRetroFit(id);
                    logger.info("SNS parsed: {}", snsMessage);
                    receivedEvents.add(snsMessage);
                } else if (eventStr.contains("sqs")) {
                    logger.info("IT'S A SQS!!!");
                    logger.info("Value SQS : {}", value);
                    logger.info("Class SQS : {}", value.getClass());
                    var body = value.get("body");
                    logger.info("Body from SQS: {}", body);
                    logger.info("Parsing to SQSEvent");
                    var sqsMessage = parseSQSMessage(value, body);
                    logger.info("SQS parsed: {}", sqsMessage);
                    receivedEvents.add(sqsMessage);
                }
            });
        });
    }

    private SNSEvent.SNS parseSNSMessage(LinkedTreeMap snsRaw) {
        SNSEvent.SNS sns;
        sns = new SNSEvent.SNS();
        var message = snsRaw.get("Message").toString();
        sns.setMessage(message);
        var messageId = snsRaw.get("MessageId").toString();
        sns.setMessageId(messageId);
        var signature = snsRaw.get("Signature").toString();
        sns.setSignature(signature);
        sns.setSubject(snsRaw.get("Subject").toString());
        sns.setType(snsRaw.get("Type").toString());
        sns.setSignatureVersion(snsRaw.get("SignatureVersion").toString());
        sns.setSigningCertUrl(snsRaw.get("SigningCertUrl").toString());
        sns.setTopicArn(snsRaw.get("TopicArn").toString());
        sns.setUnsubscribeUrl(snsRaw.get("UnsubscribeUrl").toString());
        sns.setMessageAttributes(setSNSMessageAttributes((LinkedTreeMap) snsRaw.get("MessageAttributes")));
        sns.setTimestamp(DateTime.parse(snsRaw.get("Timestamp").toString()));
        return sns;
    }

    private Map<String, SNSEvent.MessageAttribute> setSNSMessageAttributes(LinkedTreeMap messageAttributes) {
        var messageAttr = new HashMap<String, SNSEvent.MessageAttribute>();
        messageAttributes.forEach((k, v) -> messageAttr.put(k.toString(), setSNSMessageAttributesValues((LinkedTreeMap) v)));
        return messageAttr;
    }

    private SNSEvent.MessageAttribute setSNSMessageAttributesValues(LinkedTreeMap linkedTreeMap) {
        var messageAttribute = new SNSEvent.MessageAttribute();
        linkedTreeMap.forEach((k, v) -> {
            if (k.toString().equalsIgnoreCase("type")) {
                messageAttribute.setType(v.toString());
            } else if (k.toString().equalsIgnoreCase("value")) {
                messageAttribute.setValue(v.toString());
            }
        });
        return messageAttribute;
    }

    private SQSEvent.SQSMessage parseSQSMessage(LinkedTreeMap value, Object body) {
        SQSEvent.SQSMessage sqsMessage;
        String messageId;
        sqsMessage = new SQSEvent.SQSMessage();
        sqsMessage.setBody(body.toString());
        messageId = value.get("messageId").toString();
        sqsMessage.setMessageId(messageId);
        sqsMessage.setAwsRegion(value.get("awsRegion").toString());
        sqsMessage.setEventSourceArn(value.get("eventSourceARN").toString());
        sqsMessage.setEventSource(value.get("eventSource").toString());
        sqsMessage.setMd5OfMessageAttributes(value.get("md5OfMessageAttributes").toString());
        sqsMessage.setMd5OfBody(value.get("md5OfBody").toString());
        sqsMessage.setReceiptHandle(value.get("receiptHandle").toString());
        setSQSAttributes(value, sqsMessage);
        setSQSMessageAttributeMap(value, sqsMessage);
        return sqsMessage;
    }

    private void setSQSMessageAttributeMap(LinkedTreeMap value, SQSEvent.SQSMessage sqsMessage) {
        LinkedTreeMap messageAttributes = (LinkedTreeMap) value.get("messageAttributes");
        Map<String, SQSEvent.MessageAttribute> sqsMessageAttributes = new HashMap<>();
        messageAttributes.forEach((aK, aV) -> {
            var messageAttribute = new SQSEvent.MessageAttribute();
            setSQSMessageAttribute((LinkedTreeMap<?, ?>) aV, messageAttribute);
            sqsMessageAttributes.put((String) aK, messageAttribute);
        });
        sqsMessage.setMessageAttributes(sqsMessageAttributes);
    }

    private void setSQSMessageAttribute(LinkedTreeMap<?, ?> aV, SQSEvent.MessageAttribute messageAttribute) {
        String stringValue = aV.get("stringValue").toString();
        messageAttribute.setStringValue(stringValue);
        messageAttribute.setStringListValues((List<String>) aV.get("stringListValues"));
        messageAttribute.setBinaryValue(ByteBuffer.wrap(stringValue.getBytes()));
        messageAttribute.setDataType(aV.get("dataType").toString());
        messageAttribute.setBinaryListValues((List<ByteBuffer>) aV.get("binaryListValues"));
    }

    private void setSQSAttributes(LinkedTreeMap value, SQSEvent.SQSMessage sqsMessage) {
        LinkedTreeMap attributes = (LinkedTreeMap) value.get("attributes");
        Map<String, String> sqsAttributes = new HashMap<>();
        attributes.forEach((aK, aV) -> sqsAttributes.put((String) aK, (String) aV));
        sqsMessage.setAttributes(sqsAttributes);
    }

    private void sendToRetroFit(String id) {
        logger.info("Initiating retrofit");
        //Send to Retrofit
        var retroFitAPI = new RetroFitAPI();
        var retrofit = retroFitAPI.getRetrofit();
        var invokedFunctionArn = context.getInvokedFunctionArn();
        logger.info("context.getInvokedFunctionArn(): {}", invokedFunctionArn);
        logger.info("context.getFunctionName(): {}", context.getFunctionName());
        String env = invokedFunctionArn.trim().substring(invokedFunctionArn.lastIndexOf(":") + 1);
        logger.info("SNS ENV: {}", env);
        String baseUrl = getBaseUrl(env);
        var apIfz = retrofit.create(APIfz.class);
        var url = "/v3/kabc/item/" + id + "?key=otv.web.kabc.story";
        String requestUrl = baseUrl + url;
        logger.info("Sending Retrofit to: {}", requestUrl);
        Call<ClassificationDTO> call = apIfz.getId(url);
        Response<ClassificationDTO> response;
        try {
            response = call.execute();
            if (response.isSuccessful()) {
                logger.info("Received response from Retrofit");
                logger.info("Response: {}", response.body());
            } else {
                logger.info("ERROR: Something wrong with the response of Retrofit");
            }
        } catch (IOException e) {
            logger.error(String.format("ERROR: %s", e.getMessage()));
        }
    }
}