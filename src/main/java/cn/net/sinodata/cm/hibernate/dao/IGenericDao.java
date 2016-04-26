package cn.net.sinodata.cm.hibernate.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

public interface IGenericDao<T> {
	
	void save(T t);
	
	void save(List<T> list);
	
	void insert(T t);

	void delete(T t);

	void update(T t);

	T queryById(String id);

	List<T> queryAll();

	void delete(List<T> list);
}
