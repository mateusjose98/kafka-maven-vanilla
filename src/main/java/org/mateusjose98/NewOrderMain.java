package org.mateusjose98;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(
                properties()
        );
        int i = 1;
        while(i <= 15) {
            var value = "2,700,50";
            var record = new ProducerRecord<>(
                    "ECOMMERCE_PLACE_ORDER",
                    UUID.randomUUID().toString(),
                    value);

            var email = "Thank you for your order! We are processing your order!";
            var emailRecord = new ProducerRecord<>(
                    "ECOMMERCE_SEND_EMAIL",
                    UUID.randomUUID().toString(),
                    email);
            producer.send(record, getCallback()).get();
            producer.send(emailRecord, getCallback()).get();
            Thread.sleep(1000);
            i++;
        }
        producer.close();
    }

    private static Callback getCallback() {
        return (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println(data.topic() +
                    ":::partition " + data.partition() +
                    "/ offset " + data.offset() +
                    "/ timestamp " + data.timestamp());
        };
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }
}
