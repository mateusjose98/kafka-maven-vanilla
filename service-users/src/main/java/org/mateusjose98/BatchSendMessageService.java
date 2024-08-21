package org.mateusjose98;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BatchSendMessageService {
    private final Connection connection;
    private final KafkaDispatcher<User> userKafkaDispatcher = new KafkaDispatcher<>();
    public BatchSendMessageService()  {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:target/users_database.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void parse(ConsumerRecord<String, Message<String>> message) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("Processing new batch of messages");
        System.out.println("Tópico: " + message.value());
        for(User user : getAllUsers()) {
            userKafkaDispatcher.send(message.value().getPayload(), // USER_GENERATE_REPORT
                    user.getUuid(),
                    user,
                    message.value().getId().continueWith(BatchSendMessageService.class
                            .getSimpleName()),
                    null);
        }
    }

    private List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        var results = connection.prepareStatement("select uuid from Users").executeQuery();
        while (results.next()) {
            users.add(new User(results.getString(1)));
        }
        return users;
    }

    public static void main(String[] args) {
        var batchSendMessageService = new BatchSendMessageService();
        try ( var service = new KafkaService<>(
                        BatchSendMessageService.class.getSimpleName(),
                        KAKFA_CONSTANTS.ECOMMERCE_SEND_MESSAGE_TO_ALL_USERS,
                        batchSendMessageService::parse,
                        String.class,
                        Map.of())
                ) {
            service.run();
        }

    }
}
