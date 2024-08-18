package org.mateusjose98;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher<T> implements Closeable {
    private final KafkaProducer<String, T> producer;

    KafkaDispatcher() {
        this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        return properties;
    }

    public void send(String topic, String key, T value, Callback callback) throws ExecutionException, InterruptedException {
        var record = new ProducerRecord<>(
                topic,
                key,
                value);
        producer.send(record, callback == null ? defaultCallback() : callback).get();
    }


    private static Callback defaultCallback() {
        return (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("Callback >> " + data.topic() +
                    ":::partition " + data.partition() +
                    "/ offset " + data.offset() +
                    "/ timestamp " + data.timestamp());
        };
    }

    public void close() {
        producer.close();
    }
}
