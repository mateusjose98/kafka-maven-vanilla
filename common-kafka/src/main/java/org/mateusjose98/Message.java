package org.mateusjose98;

public class Message<T> {

    private final CorrelationId id;
    private final T payload;

    public Message(CorrelationId id, T payload) {
        this.id = id;
        this.payload = payload;
    }

    public CorrelationId getId() {
        return id;
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("id=").append(id);
        sb.append(", payload=").append(payload);
        sb.append('}');
        return sb.toString();
    }
}
