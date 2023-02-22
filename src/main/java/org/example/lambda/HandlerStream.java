package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Getter;
import org.joda.time.DateTime;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.utils.LambdaUtils.initializeLogger;

public class HandlerStream implements RequestStreamHandler {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    LambdaLogger logger;
    @Getter
    List<Object> receivedEvents = new ArrayList<>();

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        logger = initializeLogger(context);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))) {
            // Unit test purpose only
//            logger.log("EVENT inputstream: " + IOUtils.toString(inputStream));
            HashMap event = gson.fromJson(reader, HashMap.class);
            logger.log("STREAM TYPE: " + inputStream.getClass().toString());
            logger.log("EVENT TYPE: " + event.getClass().toString());
            logger.log("EVENT: " + event.toString());
            event.forEach((k, v) -> {
                logger.log(k.toString());
                logger.log("Key class" + k.getClass());
                logger.log("Value class:" + v.getClass());
                List<LinkedTreeMap> list = (List) v;
                logger.log("Size list:" + list.size());
                logger.log("Class : " + ((List<?>) v).get(0).getClass());
                logger.log("LinkedTreeMap class ?" + list.getClass());
                filterByEventType(receivedEvents, list);
            });
        }
    }

    private void filterByEventType(List<Object> receivedEvents, List<LinkedTreeMap> list) {
        //Vemos cada uno de los mensajes que vienen
        list.forEach(value -> {
            logger.log("A value of list :" + value);
            logger.log("Class : " + value.getClass());
            var eventSource = value.keySet().stream()
                    .filter(key -> key.toString().equalsIgnoreCase("eventsource"))
                    .findFirst();
            eventSource.ifPresent(aEvent -> {
                var theEvent = value.get(aEvent);
                logger.log("EventSource: " + theEvent);
                logger.log("EventSource class: " + theEvent.getClass());
                String eventStr = theEvent.toString();
                if (eventStr.contains("sns")) {
                    logger.log("IT'S A SNS!!!");
                    LinkedTreeMap sns = (LinkedTreeMap) value.get("Sns");
                    logger.log("Value SNS : " + sns.toString());
                    logger.log("Class SNS : " + sns.getClass());
                    logger.log("Parsing to SNSEvent");
                    var snsMessage = parseSNSMessage(sns);
                    logger.log("SNS parsed: " + snsMessage.toString());
                    receivedEvents.add(snsMessage);

                } else if (eventStr.contains("sqs")) {
                    logger.log("IT'S A SQS!!!");
                    logger.log("Value SQS : " + value.toString());
                    logger.log("Class SQS : " + value.getClass());
                    var body = value.get("body");
                    logger.log("Body from SQS: " + body);
                    logger.log("Parsing to SQSEvent");
                    var sqsMessage = parseSQSMessage(value, body);
                    logger.log("SQS parsed: " + sqsMessage.toString());
                    receivedEvents.add(sqsMessage);
                }
            });
        });
    }

    private SNSEvent.SNS parseSNSMessage(LinkedTreeMap snsRaw) {
        var sns = new SNSEvent.SNS();
        sns.setMessage(snsRaw.get("Message").toString());
        sns.setMessageId(snsRaw.get("MessageId").toString());
        sns.setSignature(snsRaw.get("Signature").toString());
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
        messageAttributes.forEach((k, v) -> {
            messageAttr.put(k.toString(), setSNSMessageAttributesValues((LinkedTreeMap) v));
        });
        return null;
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
        SQSEvent.SQSMessage sqsMessage = new SQSEvent.SQSMessage();
        sqsMessage.setBody(body.toString());
        sqsMessage.setMessageId(value.get("messageId").toString());
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
        List<String> stringValues = new ArrayList<>();
        LinkedTreeMap<?, ?> aLinkedTreeMap = aV;
        String stringValue = aLinkedTreeMap.get("stringValue").toString();
        messageAttribute.setStringValue(stringValue);
        messageAttribute.setStringListValues((List<String>) aLinkedTreeMap.get("stringListValues"));
        messageAttribute.setBinaryValue(ByteBuffer.wrap(stringValue.getBytes()));
        messageAttribute.setDataType(aLinkedTreeMap.get("dataType").toString());
        messageAttribute.setBinaryListValues((List<ByteBuffer>) aLinkedTreeMap.get("binaryListValues"));
    }

    private void setSQSAttributes(LinkedTreeMap value, SQSEvent.SQSMessage sqsMessage) {
        LinkedTreeMap attributes = (LinkedTreeMap) value.get("attributes");
        Map<String, String> sqsAttributes = new HashMap<>();
        attributes.forEach((aK, aV) -> sqsAttributes.put((String) aK, (String) aV));
        sqsMessage.setAttributes(sqsAttributes);
    }
}