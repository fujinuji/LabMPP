package scs.ubb.mpp.repository;

import scs.ubb.mpp.entity.Entity;

import java.util.List;

public interface Repository<T extends Entity<ID>, ID> {
    List<T> getAll();

    T getById(ID id);

    void save(T entity);

    void update(ID id, T entity);

    void delete(ID id);
}
