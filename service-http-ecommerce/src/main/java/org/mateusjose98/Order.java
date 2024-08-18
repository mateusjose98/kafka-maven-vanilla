package org.mateusjose98;

import java.math.BigDecimal;

public class Order {
    private final String orderId;
    private final BigDecimal amount;
    private final String email;

    public Order(String orderId, BigDecimal amount, String email) {
        this.orderId = orderId;
        this.amount = amount;
        this.email = email;
    }


    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public String getEmail() {
        return email;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("orderId='").append(orderId).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }



}
