package org.mateusjose98;

import org.apache.kafka.clients.consumer.*;


public class FraudDetectorService {


    private void parse(ConsumerRecord<String, String> record) {
        String message = String.format("Processing order, checking for fraud. Key: %s, Value: %s, Partition: %d, Offset: %d",
                record.key(), record.value(), record.partition(), record.offset());
        System.out.println(message);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Order processed");
    }

    public static void main(String[] args) {
        try(var s = new KafkaService(
                FraudDetectorService.class.getSimpleName(),
                KAKFA_CONSTANTS.ECOMMERCE_PLACE_ORDER,
                new FraudDetectorService()::parse)) {
            s.run();
        }

    }

}
