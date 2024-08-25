package org.mateusjose98;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class EmailNewOrderService implements ConsumerService<Order> {

    public static void main(String[] args){
        new ServiceRunner<>(EmailNewOrderService::new).start(1);
    }

    private final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<>();

    public void parse(ConsumerRecord<String, Message<Order>> record)
            throws Exception {
        System.out.println("------------------------------------------");
        System.out.println("Processing NEW ORDER, preparing email");
        var message = record.value();
        System.out.println(message);

        var order = message.getPayload();
        var emailBody = order.getEmail() + ", thank you for your order! We are processing your order!";
        var id = message.getId().continueWith(EmailNewOrderService.class.getSimpleName());
        emailDispatcher.send(
                KAKFA_CONSTANTS.ECOMMERCE_SEND_EMAIL,
                order.getEmail(),
                new Email(order.getEmail(), emailBody),
                id,
                null);

    }

    @Override
    public String getTopic() {
        return KAKFA_CONSTANTS.ECOMMERCE_PLACE_ORDER;
    }


    @Override
    public String getConsumerGroup() {
        return EmailNewOrderService.class.getSimpleName();
    }

    @Override
    public Class getType() {
        return Order.class;
    }
}
