package org.mateusjose98;

public interface ServiceFactory<T> {
    ConsumerService<T> create();
}
