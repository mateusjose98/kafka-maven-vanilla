package org.mateusjose98;

import java.math.BigDecimal;

public class Order {
    private final String orderId, email;
    private final BigDecimal amount;

    public Order(String orderId, String email, BigDecimal amount) {
        this.orderId = orderId;
        this.email = email;
        this.amount = amount;
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
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}
