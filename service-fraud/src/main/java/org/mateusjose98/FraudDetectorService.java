package org.mateusjose98;

import org.apache.kafka.clients.consumer.*;

import java.util.HashMap;


public class FraudDetectorService {

    private void parse(ConsumerRecord<String, Order> record) {
        String message = String.format("Processing order, checking for fraud. Key: %s, Value: %s, Partition: %d, Offset: %d",
                record.key(), record.value(), record.partition(), record.offset());
        System.out.println(message);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Order processed");
    }

    public static void main(String[] args) {
        try(var service = new KafkaService<Order>(
                FraudDetectorService.class.getSimpleName(),
                KAKFA_CONSTANTS.ECOMMERCE_PLACE_ORDER,
                new FraudDetectorService()::parse,
                Order.class,
                new HashMap<>())) {
            service.run();
        }

    }

}
