package org.mateusjose98;

import java.util.UUID;

public class CorrelationId {

    private final String id;

    public CorrelationId(String title) {
        this.id = title + "_" + UUID.randomUUID();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CorrelationId{");
        sb.append("id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public CorrelationId continueWith(String title) {
        return new CorrelationId(this.id + "_" + title);
    }
}
