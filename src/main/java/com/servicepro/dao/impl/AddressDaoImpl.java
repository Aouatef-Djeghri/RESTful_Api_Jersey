package com.servicepro.dao.impl;

import java.util.ArrayList;

import org.hibernate.Session;

import com.servicepro.dao.interfaces.AddressDao;
import com.servicepro.entities.Address;
import com.servicepro.exception.DataNotFoundException;
import com.servicepro.exception.DatabaseException;
import com.servicepro.config.HibernateUtil;



/*
 * Aouatef Djeghri
 * Year 2020
 */



public class AddressDaoImpl implements AddressDao {

	Session session;

	@Override
	public int add(Address obj) {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			int id = (int) session.save(obj);
			session.getTransaction().commit();
			session.close();
			return id;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException Address " + e.getMessage());
		}
	}

	@Override
	public int deleteById(int id) {

		if (findById(id) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Address with id :" + id + " not found  !");
		}
		try {
			Address c = findById(id);
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.delete(c);
			session.getTransaction().commit();
			session.close();
			return 1;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException Address " + e.getMessage());
		}

	}

	@Override
	public void deleteByQuery(Address t) {
		//not used
	}

	@Override
	public boolean edit(Address address) {

		if (address == null) {
			// throw exception user not found
			throw new DataNotFoundException("Address cannot be null! ");
		}

		if (findById(address.getIdAddress()) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Address with id :" + address.getIdAddress() + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.update(address);
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException Address " + e.getMessage());
		}

	}

	@Override
	public Address findById(int i) {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			Address c = (Address) session.get(Address.class, i);
			session.getTransaction().commit();
			session.close();
			if (c == null) {
				// throw exception Address not found
				throw new DataNotFoundException("Address with id : " + i + " not found  !");
			}
			return c;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public ArrayList<Address> listAll() {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Address> l = (ArrayList<Address>) session.createQuery("from Address").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

}
