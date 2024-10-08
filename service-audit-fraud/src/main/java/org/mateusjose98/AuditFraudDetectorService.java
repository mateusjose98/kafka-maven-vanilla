package org.mateusjose98;

import org.apache.kafka.clients.consumer.*;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class AuditFraudDetectorService {

    private void parse(ConsumerRecord<String, Order> record) {
        String message = String.format("AUDITORIA >> . Key: %s, Value: %s, Partition: %d, Offset: %d",
                record.key(), record.value(), record.partition(), record.offset());
        System.out.println(message);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try(var service = new KafkaService(
                "A",
                KAKFA_CONSTANTS.ECOMMERCE_PLACE_ORDER,
                new AuditFraudDetectorService()::parse,
                Order.class,
                new HashMap<>())) {
            service.run();
        }

    }

}
