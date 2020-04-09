package scs.ubb.mpp.entity;

import java.io.Serializable;

public class Entity<T> implements Serializable {
    private T id;

    public Entity(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }
}
