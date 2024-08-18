package org.mateusjose98;

import org.apache.kafka.clients.consumer.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class FraudDetectorService {
    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<Order>();
    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        String message = String.format("Processing order, checking for fraud. Key: %s, Value: %s, Partition: %d, Offset: %d",
                record.key(), record.value(), record.partition(), record.offset());
        System.out.println(message);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Order order = record.value();

        if (isFraud(order)) {
            System.out.println("Order is a fraud!");
            orderDispatcher.send(
                    KAKFA_CONSTANTS.ECOMMERCE_ORDER_REJECTED,
                    order.getEmail(), order, null);
        } else {
            System.out.println("Approved: " + order);
            orderDispatcher.send(KAKFA_CONSTANTS.ECOMMERCE_ORDER_APPROVED,
                    order.getEmail(),
                    order, null);
        }

    }

    private static boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("1500")) >= 0;
    }

    public static void main(String[] args) {
        try(var service = new KafkaService<Order>(
                "A",
                KAKFA_CONSTANTS.ECOMMERCE_PLACE_ORDER,
                new FraudDetectorService()::parse,
                Order.class,
                new HashMap<>())) {
            service.run();
        }



    }

}
