package org.mateusjose98;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        var orderDispatcher = new KafkaDispatcher<Order>();


        int i = 1;
        var email = "email_" + (Math.random() * 10 + 1) + "@gmail.com";
        while(i <= 10) {

            var orderId = UUID.randomUUID().toString();
            var amount = new BigDecimal(Math.random() * 5000 + 1);
            var order = new Order(orderId, amount, email);
            orderDispatcher.send(KAKFA_CONSTANTS.ECOMMERCE_PLACE_ORDER,
                    email,
                    order,
                    new CorrelationId(NewOrderMain.class.getSimpleName()),
                    getCallback());
            Thread.sleep(100);
            Thread.sleep(100);
            i++;
        }
        orderDispatcher.close();

    }

    private static Callback getCallback() {
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


}
