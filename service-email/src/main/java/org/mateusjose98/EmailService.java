package org.mateusjose98;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;

public class EmailService {

    private void parse(ConsumerRecord<String, Email> record) {
        System.out.printf("Sending EMAIL. Key: %s, Value: %s, Partition: %d, Offset: %d%n", record.key(), record.value(), record.partition(), record.offset());
    }

    public static void main(String[] args) {
        EmailService emailService = new EmailService();
        try (KafkaService<Email> service = new KafkaService(
                EmailService.class.getSimpleName(),
                KAKFA_CONSTANTS.ECOMMERCE_SEND_EMAIL,
                emailService::parse,
                Email.class,
                new HashMap<>())) {
            service.run();
        }
    }

}
