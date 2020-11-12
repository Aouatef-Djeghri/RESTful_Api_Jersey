package com.servicepro.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.servicepro.config.HibernateUtil;
import com.servicepro.dao.interfaces.CategorieDao;
import com.servicepro.dao.interfaces.ServiceDao;
import com.servicepro.dao.interfaces.UserDao;
import com.servicepro.dao.interfaces.WorkDao;
import com.servicepro.entities.Service;
import com.servicepro.entities.User;
import com.servicepro.entities.Work;
import com.servicepro.exception.DataAlreadyExistsException;
import com.servicepro.exception.DataNotFoundException;
import com.servicepro.exception.DatabaseException;
import com.servicepro.exception.EmptyParameterException;



/*
 * Aouatef Djeghri
 * Year 2020
 */




public class ServiceDaoImpl implements ServiceDao {

	Session session;
	CategorieDao categorieDao = HibernateUtil.getCategorieDao();

	UserDao userDao = HibernateUtil.getUserDao();

	
	
	@Override
	public int add(Service service) {
		
		// test if service name is empty or null
		if (service.getName().equals("") || service.getName() == null) {
			throw new EmptyParameterException("Service Name cannot be null or empty");
		}

		// test if service status is empty or null
		if (service.getStatus().equals("") || service.getStatus() == null) {
			throw new EmptyParameterException("Service Status cannot be null or empty");
		}

		// test if service status is not 'available' nor 'unavailable'
		if (!service.getStatus().equals("available") && !service.getStatus().equals("unavailable")) {
			throw new DataNotFoundException("Service Status must be either 'available' or 'unavailable'");
		}

		// test if service category is not null
		if (service.getCategorie() == null) {
			throw new EmptyParameterException("Service Categorie cannot be null");
		}
		
		// test if service 'idCategory' is not null
		if (service.getCategorie().getIdCategorie() == null) {
			throw new EmptyParameterException("Service IdCategory cannot be null");
		}

		// test if service category dosn't exists
		if (categorieDao.findById(service.getCategorie().getIdCategorie()) == null) {
			// throw exception Category not found
			throw new DataNotFoundException(
					"Service Categorie with id :" + service.getCategorie().getIdCategorie() + " not found  !");
		}
		
		
		// test if service name already exists
		int ifServiceExistId = isServiceNameAlreadyExists(service);
		if (ifServiceExistId!=0) {
			Service s = findById(ifServiceExistId);
			if(s.getStatus().equals("available")) {
				throw new DataAlreadyExistsException("Service name: " + service.getName() + " already exists");
			}else{
				s.setStatus("available");
				edit(s);
				return s.getIdService();
			}
		}else {
			
			try {
				session = HibernateUtil.getSession();
				session.beginTransaction();
				int id = (int) session.save(service);
				session.getTransaction().commit();
				session.close();
				return id;
			} catch (Exception e) {
				throw new DatabaseException(e.getMessage());
			}
		}

	}

	public int isServiceNameAlreadyExists(Service service) {

		ArrayList<Service> serviceList = listAll();
		for (int i = 0; i < serviceList.size(); i++) {
			if (service.getName().equals(serviceList.get(i).getName())) {
				return serviceList.get(i).getIdService();
			}
		}
		return 0;
		}

	@Override
	public int deleteById(int id) {
		// test if Service exists or not
		if (findById(id) == null) {
			throw new DataNotFoundException("Service with id :" + id + " not found  !");
		}
		Service service = findById(id);
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.delete(service);
			session.getTransaction().commit();
			session.close();
			return 1;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public void deleteService(int id) {

		// test if Service dosn't exists
		if (findById(id) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Service with id :" + id + " not found  !");
		}

		// if Service, exist get it
		Service service = findById(id);

		if (isServicesUsedInWOrk(service)) {
			// if service is used in work its status become
			// unavailable and it cannot be deleted from db
			service.setStatus("unavailable");
			edit(service);

		} else {
			// if service is not used in work it can be deleted
			deleteById(id);

		}

	}

	public boolean isServicesUsedInWOrk(Service service) {
	
		WorkDao workDao = HibernateUtil.getWorkDao();
		
		// get all Work list
		ArrayList<Work> workList = workDao.listAll();
		for (int i = 0; i < workList.size(); i++) {

			if (workList.get(i).getService().getName().equals(service.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void deleteByQuery(Service t) {
		// Not implemented yet
	}

	@Override
	public boolean edit(Service service) {

		if (service == null) {
			// throw exception Service not found
			throw new DataNotFoundException("Service cannot be null! ");
		}

		// test if service 'id' is not null
		if (service.getIdService() == null) {
			throw new EmptyParameterException("Service Id cannot be null");
		}
		
		// test if Service dosn't exists
		if (findById(service.getIdService()) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Service with id :" + service.getIdService() + " not found  !");
		}

		// test if service name is empty or null
		if (service.getName().equals("") || service.getName() == null) {
			throw new EmptyParameterException("Service Name cannot be null or empty");
		}

		// test if service status is empty or null
		if (service.getStatus().equals("") || service.getStatus() == null) {
			throw new EmptyParameterException("Service Status cannot be null or empty");
		}

		// test if service status is not 'available' nor 'unavailable'
		if (!service.getStatus().equals("available") && !service.getStatus().equals("unavailable")) {
			throw new DataNotFoundException("Service Status must be either 'available' or 'unavailable'");
		}

		// test if service name exists but its the same (category has the same name)
		if (isServiceNameChanged(service)) {
				// test if service name already exists
				int ifServiceExistId = isServiceNameAlreadyExists(service);
				if (ifServiceExistId!=0) {
					throw new
					  DataAlreadyExistsException("Service name: " + service.getName() +
					  " already exists");
				}
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.update(service);
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException Service" + e.getMessage());
		}
	}

	public boolean isServiceNameChanged(Service service) {

		String oldServiceName = findById(service.getIdService()).getName();
		if (service.getName().equals(oldServiceName)) {
			return false;
		}
		return true;
	}

	@Override
	public Service findById(int i) {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			Service c = (Service) session.get(Service.class, i);
			session.getTransaction().commit();
			session.close();
			if (c == null) {
				// throw exception Service not found
				throw new DataNotFoundException("Service with id : " + i + " not found  !");
			}
			return c;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public ArrayList<Service> listAll() {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Service> l = (ArrayList<Service>) session.createQuery("from Service").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public ArrayList<Service> listAllAvailableServices() {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Service> l = (ArrayList<Service>) session
					.createQuery("from Service as s where s.status = 'available' ").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public ArrayList<Service> listAllUnavailableServices() {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Service> l = (ArrayList<Service>) session
					.createQuery("from Service as s where s.status = 'unavailable' ").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public ArrayList<Service> listAllServicesByCategorie(int categorieId) {


		
		// test if category exists or not
		if (categorieDao.findById(categorieId) == null) {
			// throw exception category not found
			throw new DataNotFoundException("Category with id :" + categorieId + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Service> l = (ArrayList<Service>) session
					.createQuery("from Service as s where s.categorie.idCategorie = " + categorieId + "").list();
			session.getTransaction().commit();
			session.close();
			return l;

		} catch (Exception e) {

			throw new DatabaseException("Something in database went wrong !" + e.getMessage());
		}

	}

	@Override
	public ArrayList<Service> listAllAvailableServicesByCategorie(int categorieId) {

		
		// test if category exists or not
		if (categorieDao.findById(categorieId) == null) {
			// throw exception category not found
			throw new DataNotFoundException("Category with id :" + categorieId + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Service> l = (ArrayList<Service>) session.createQuery(
					"from Service as s where s.categorie.idCategorie = " + categorieId + " and s.status = 'available'")
					.list();
			session.getTransaction().commit();
			session.close();
			return l;

		} catch (Exception e) {

			throw new DatabaseException("Something in database went wrong !" + e.getMessage());
		}
	}

	@Override
	public ArrayList<Service> listAllUnavailableServicesByCategorie(int categorieId) {
		
		// test if category exists or not
		if (categorieDao.findById(categorieId) == null) {
			// throw exception category not found
			throw new DataNotFoundException("Category with id :" + categorieId + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Service> l = (ArrayList<Service>) session
					.createQuery("from Service as s where s.categorie.idCategorie = " + categorieId
							+ " and s.status = 'unavailable'")
					.list();
			session.getTransaction().commit();
			session.close();
			return l;

		} catch (Exception e) {

			throw new DatabaseException("Something in database went wrong !" + e.getMessage());
		}
	}

	@Override
	public List<Service> updateServicesList(int idUser, List<Service> services) {


		// test if user exists or not
		if (userDao.findById(idUser) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id :" + idUser + " not found  !");
		}
		User user = userDao.findById(idUser);
		// inserting the new list
		Set<Service> set = new HashSet<Service>(services);
		user.setServices(set);
		userDao.edit(user);
		return services;
	}

	@Override
	public ArrayList<Service> getServicesArtisan(int idUser) {

		
		// test if user exists or not
		if (userDao.findById(idUser) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id :" + idUser + " not found  !");
		}
		User user = userDao.findById(idUser);

		ArrayList<Service> userAllServicesList = new ArrayList<Service>(user.getServices());
		ArrayList<Service> userAvailableServicesList = new ArrayList<Service>();

		for (int i = 0; i < userAllServicesList.size(); i++) {
			if (userAllServicesList.get(i).getStatus().equals("available")) {
				userAvailableServicesList.add(userAllServicesList.get(i));
			}
		}

		return userAvailableServicesList;

	}

	@Override
	public boolean deleteAllCategoryServices(int categorieId) {
		

		
		// test if category exists or not
		if (categorieDao.findById(categorieId) == null) {
			// throw exception category not found
			throw new DataNotFoundException("Category with id :" + categorieId + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			String q = "delete from Service as s where s.categorie.idCategorie = " + categorieId;
			session.createQuery(q).executeUpdate();
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {

			throw new DatabaseException("Something went wrong when deleting services ! " + e.getMessage());
		}

	}

	@Override
	public void putServicesUnavailable(int categorieId) {

		
		// test if category exists or not
		if (categorieDao.findById(categorieId) == null) {
			// throw exception category not found
			throw new DataNotFoundException("Category with id :" + categorieId + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			String q = "update Service set status = 'unavailable' where categorie.idCategorie = " + categorieId;
			session.createQuery(q).executeUpdate();
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {

			throw new DatabaseException("Something went wrong when putting services unavailable ! " + e.getMessage());
		}

	}

}
