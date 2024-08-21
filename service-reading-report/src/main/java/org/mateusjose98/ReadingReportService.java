package org.mateusjose98;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

public class ReadingReportService {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();
    private void parse(ConsumerRecord<String, Message<User>> record) throws Exception {
        System.out.printf("Processing REPORT for user Value: %s, Partition: %d, Offset: %d%n",
                record.value(),
                record.partition(),
                record.offset());
        User user = record.value().getPayload();
        var target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);
        IO.append(target, "Created for " + user.getUuid());


    }

    public static void main(String[] args) {
        ReadingReportService readingReportService = new ReadingReportService();
        try (KafkaService<User> service = new KafkaService(
                ReadingReportService.class.getSimpleName(),
                KAKFA_CONSTANTS.ECOMMERCE_USER_GENERATE_REPORT,
                readingReportService::parse,
                User.class,
                new HashMap<>())) {
            service.run();
        }
    }
}