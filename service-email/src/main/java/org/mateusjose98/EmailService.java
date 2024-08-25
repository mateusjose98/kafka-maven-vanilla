package org.mateusjose98;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class EmailService implements ConsumerService<Email> {

    public void parse(ConsumerRecord<String, Message<Email>> record) {
        System.out.printf("Sending EMAIL. Key: %s, Value: %s, Partition: %d, Offset: %d%n",
                record.key(), record.value(), record.partition(), record.offset());
    }

    public String getTopic() {
        return KAKFA_CONSTANTS.ECOMMERCE_SEND_EMAIL;
    }


    public String getConsumerGroup() {
        return EmailService.class.getSimpleName();
    }

    @Override
    public Class getType() {
        return Email.class;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ServiceRunner(EmailService::new).start(5);
    }

}
