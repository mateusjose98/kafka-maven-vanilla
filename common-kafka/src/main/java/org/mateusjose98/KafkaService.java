package org.mateusjose98;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {
    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction<T> parser;
    public KafkaService(String groupName, String topic, ConsumerFunction<T> parser, Class<T> type,
                        Map<String, String> properties) {
        this(groupName, parser, type, properties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaService(String groupName, Pattern pattern, ConsumerFunction<T> parser,
                        Class<T> type, Map<String, String> properties) {
        this(groupName, parser, type, properties);
        consumer.subscribe(pattern);
    }

    private KafkaService(String groupName, ConsumerFunction<T> parser, Class<T> type,  Map<String, String> properties) {
        this.parser = parser;
        this.consumer = new KafkaConsumer<>(getProperties(groupName, type, properties));
    }


    public void run() {
        while(true) {
            ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));
            if(!records.isEmpty()){
                System.out.println("Qtde. " + records.count() + " registros");
                for (var record : records) {
                    this.parser.consume(record);
                }
            }
        }
    }
    private Properties getProperties(String groupName, Class<T> type,
                                     Map<String, String> overrideProperties) {
        var props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAKFA_CONSTANTS.BOOTSTRAP_SERVERS);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupName);
        props.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, groupName +"_"+ UUID.randomUUID());
        props.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());

        props.putAll(overrideProperties);
        return props;
    }

    @Override
    public void close()  {
        consumer.close();
    }
}
