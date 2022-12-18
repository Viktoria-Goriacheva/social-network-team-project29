package ru.socialnet.team29.repository;

import java.util.List;

public abstract class AbstractRepository<T> {

    public abstract int insert(T t);

    public abstract T findById(int id);

    public abstract boolean update(T t);

    public abstract boolean delete(int id);

}
