package org.mateusjose98;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) {
        var consumer = new KafkaConsumer<String, String>(properties());

        consumer.subscribe(Pattern.compile("ECOMMERCE.*"));


        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            if(!records.isEmpty()){
                records.forEach(record -> {
                    System.out.println("[log] - " + record.topic() +" Key= " + record.key() + "  Value= " + record.value()
                            +" Partition="+ record.partition() + " Offset= " + record.offset());
                });

            }
        }



    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, KAKFA_CONSTANTS.GROUP_ID);

        return properties;
    }
}
