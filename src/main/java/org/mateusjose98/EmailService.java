package org.mateusjose98;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class EmailService {

    private void parse(ConsumerRecord<String, String> record) {
            System.out.println("Sending email");
            System.out.println("Key: " + record.key());
            System.out.println("Value: " + record.value());
            System.out.println("Partition: " + record.partition());
            System.out.println("Offset: " + record.offset());
    }

    public static void main(String[] args) {
        EmailService emailService = new EmailService();
        try (KafkaService service = new KafkaService(EmailService.class.getSimpleName(), KAKFA_CONSTANTS.ECOMMERCE_SEND_EMAIL, emailService::parse)) {
            service.run();
        }

    }

}
