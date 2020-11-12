package com.servicepro.dao.interfaces;

import java.util.ArrayList;

public interface Dao<T> {
	int add(T obj);

	int deleteById(int i);

	void deleteByQuery(T t);

	boolean edit(T obj);

	T findById(int i);

	ArrayList<T> listAll();
}
