package org.mateusjose98;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public interface ConsumerService<T> {

    String getTopic();

    void parse(ConsumerRecord<String, Message<T>> record) throws Exception;

    String getConsumerGroup();

    Class getType();
}
