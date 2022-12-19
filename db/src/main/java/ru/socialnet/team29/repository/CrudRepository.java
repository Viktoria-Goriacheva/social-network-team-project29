package ru.socialnet.team29.repository;

public interface CrudRepository<T> {

    public int insert(T t);

    public T findById(int id);

    public T update(T t);

    public boolean delete(int id);

}
