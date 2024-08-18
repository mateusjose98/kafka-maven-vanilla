package org.mateusjose98;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateUserService {
    private final Connection connection;

    CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute(
                    """
                    create table Users (
                    uuid varchar(200) primary key,
                    email varchar(200))
            """);
        } catch (SQLException ex) {
            // be careful, the sql could be wrong, be really careful
            ex.printStackTrace();
        }
    }

    private boolean isNewUser(String email) throws SQLException {
        var exists = connection.prepareStatement("select uuid from Users " +
                "where email = ? limit 1");
        exists.setString(1, email);
        var results = exists.executeQuery();
        return !results.next();
    }

    private void insertNewUser( String email) throws SQLException {
        var insert = connection.prepareStatement(
                "insert into Users (uuid, email) values (?,?)");
        insert.setString(1, UUID.randomUUID().toString());
        insert.setString(2, email);
        insert.execute();
        System.out.println("Usuário uuid e " + email + " adicionado");
    }

    private void parse(ConsumerRecord<String, Order> record) throws  SQLException {
        String message = String.format("Checking new user. Key: %s, Value: %s, Partition: %d, Offset: %d",
                record.key(), record.value(), record.partition(), record.offset());
        System.out.println(message);
        Order order = record.value();
        if(isNewUser(order.getEmail())) {
            insertNewUser(order.getEmail());
        } else {
            System.out.println("Usuário já existe");
        }

    }

    public static void main(String[] args) throws SQLException{
        CreateUserService createUserService = new CreateUserService();

        try(var service = new KafkaService<Order>(
                CreateUserService.class.getSimpleName(),
                KAKFA_CONSTANTS.ECOMMERCE_PLACE_ORDER,
                createUserService::parse,
                Order.class,
                new HashMap<>())) {
            service.run();
        }
    }



}