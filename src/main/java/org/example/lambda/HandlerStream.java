package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerStream implements RequestStreamHandler {
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = LogManager.getLogger(HandlerStream.class);
    @Getter
    final List<Object> receivedEvents = new ArrayList<>();

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))) {
            // Unit test purpose only
//            logger.info("EVENT inputstream: " + IOUtils.toString(inputStream));
            HashMap event = gson.fromJson(reader, HashMap.class);
            logger.info("STREAM TYPE: " + inputStream.getClass().toString());
            logger.info("EVENT TYPE: " + event.getClass().toString());
            logger.info("EVENT: " + event);
            event.forEach((k, v) -> {
                logger.info(k.toString());
                logger.info("Key class" + k.getClass());
                logger.info("Value class:" + v.getClass());
                List<LinkedTreeMap> list = (List) v;
                logger.info("Size list:" + list.size());
                logger.info("Class : " + ((List<?>) v).get(0).getClass());
                logger.info("LinkedTreeMap class ?" + list.getClass());
                filterByEventType(receivedEvents, list);
            });
        }
    }

    private void filterByEventType(List<Object> receivedEvents, List<LinkedTreeMap> list) {
        //Vemos cada uno de los mensajes que vienen
        list.forEach(value -> {
            logger.info("A value of list :" + value);
            logger.info("Class : " + value.getClass());
            var eventSource = value.keySet().stream()
                    .filter(key -> key.toString().equalsIgnoreCase("eventsource"))
                    .findFirst();
            eventSource.ifPresent(aEvent -> {
                var theEvent = value.get(aEvent);
                logger.info("EventSource: " + theEvent);
                logger.info("EventSource class: " + theEvent.getClass());
                String eventStr = theEvent.toString();
                if (eventStr.contains("sns")) {
                    logger.info("IT'S A SNS!!!");
                    LinkedTreeMap sns = (LinkedTreeMap) value.get("Sns");
                    logger.info("Value SNS : " + sns.toString());
                    logger.info("Class SNS : " + sns.getClass());
                    logger.info("Parsing to SNSEvent");
                    var snsMessage = parseSNSMessage(sns);
                    logger.info("SNS parsed: " + snsMessage);
                    receivedEvents.add(snsMessage);

                } else if (eventStr.contains("sqs")) {
                    logger.info("IT'S A SQS!!!");
                    logger.info("Value SQS : " + value);
                    logger.info("Class SQS : " + value.getClass());
                    var body = value.get("body");
                    logger.info("Body from SQS: " + body);
                    logger.info("Parsing to SQSEvent");
                    var sqsMessage = parseSQSMessage(value, body);
                    logger.info("SQS parsed: " + sqsMessage);
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
}