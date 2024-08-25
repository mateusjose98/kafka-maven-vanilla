package org.mateusjose98;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ReadingReportService implements ConsumerService<User> {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();

    @Override
    public String getTopic() {
        return KAKFA_CONSTANTS.ECOMMERCE_USER_GENERATE_REPORT;
    }

    public void parse(ConsumerRecord<String, Message<User>> record) throws IOException {
        System.out.printf("Processing REPORT for user Value: %s, Partition: %d, Offset: %d%n",
                record.value(),
                record.partition(),
                record.offset());
        User user = record.value().getPayload();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + user.getUuid());


    }

    @Override
    public String getConsumerGroup() {
        return ReadingReportService.class.getSimpleName();
    }

    @Override
    public Class getType() {
        return User.class;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ServiceRunner<>(ReadingReportService::new).start(5);
    }
}