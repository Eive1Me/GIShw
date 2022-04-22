package sample.databasemanage.repo;

import sample.databasemanage.entity.BaseEntity;

import java.util.ArrayList;

public interface IRestRepository<T> {
     ArrayList<T> select();
    T select(Integer id);
    T insert(T entity);
//    T update(Integer id, T entity);
    T delete(Integer id);
}
