package org.mateusjose98;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ServiceProvider<T> implements Callable<Void> {

    private final ServiceFactory<T> factory;

    public ServiceProvider(ServiceFactory<T> factory) {
        this.factory = factory;
    }
    public Void call() throws ExecutionException,
                InterruptedException {
            var myservice = factory.create();
            try (var service = new KafkaService(
                    myservice.getConsumerGroup(),
                    myservice.getTopic(),
                    myservice::parse,
                    myservice.getType(),
                    new HashMap<>())) {
                service.run();
            }
            return null;
        }
}
