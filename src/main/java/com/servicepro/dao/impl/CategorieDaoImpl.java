package com.servicepro.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.servicepro.config.CloudinaryUtil;
import com.servicepro.config.HibernateUtil;
import com.servicepro.dao.interfaces.CategorieDao;
import com.servicepro.dao.interfaces.ServiceDao;
import com.servicepro.dao.interfaces.WorkDao;
import com.servicepro.entities.*;
import com.servicepro.exception.DataAlreadyExistsException;
import com.servicepro.exception.DataNotFoundException;
import com.servicepro.exception.DatabaseException;
import com.servicepro.exception.EmptyParameterException;


/*
 * Aouatef Djeghri
 * Year 2020
 */




public class CategorieDaoImpl implements CategorieDao {

	Session session;

	@Override
	public int add(Categorie category) {

		// test if category name is empty or null
		if (category.getName().equals("") || category.getName() == null) {
			throw new EmptyParameterException("Category Name cannot be null or empty");
		}



		// test if category status is empty or null
		if (category.getStatus().equals("") || category.getStatus() == null) {
			throw new EmptyParameterException("Category Status cannot be null or empty");
		}

		// test if category status is not 'available' nor 'unavailable'
		if (!category.getStatus().equals("available") && !category.getStatus().equals("unavailable")) {
			throw new DataNotFoundException("Category Status must be either 'available' or 'unavailable'");
		}
		
		
		// test if category name already exists
		int ifCategoryExistId = iscategoryNameAlreadyExists(category);
		if (ifCategoryExistId!=0) {
			Categorie c = findById(ifCategoryExistId);
			if(c.getStatus().equals("available")) {
				throw new DataAlreadyExistsException("Category name: " + category.getName() + " already exists");
			}
			else{
				c.setStatus("available");
				edit(c);
				return c.getIdCategorie();
			}
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			int id = (int) session.save(category);
			session.getTransaction().commit();
			session.close();
			return id;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	
	public int iscategoryNameAlreadyExists(Categorie category) {

		ArrayList<Categorie> categoryList = listAll();
		for (int i = 0; i < categoryList.size(); i++) {
			if (category.getName().equals(categoryList.get(i).getName())) {
				return categoryList.get(i).getIdCategorie();
			}
		}
		return 0;
	}


	@Override
	public int deleteById(int id) {
		// test if Category exists or not
		if (findById(id) == null) {
			throw new DataNotFoundException("Category with id :" + id + " not found  !");
		}
		Categorie category = findById(id);
		String imageURL = category.getImage();
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.delete(category);
			session.getTransaction().commit();
			session.close();
			CloudinaryUtil.deleteImage(imageURL, "category");
			return 1;
		} catch (Exception e) {
			throw new DatabaseException(" category " + e.getMessage());
		}
	}

	@Override
	public void deleteCategory(int id) {

		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		// test if Category exists or not
		if (findById(id) == null) {
			throw new DataNotFoundException("Category with id :" + id + " not found  !");
		}

		// if Category, exist get it
		Categorie category = findById(id);
		// test if category have services
		if (!category.getServices().isEmpty()) {

			// get the list of category services that are used in work
			List<Service> list = categoryServicesUsedInWorkList(category);

			if (!list.isEmpty()) {
				// if category have services that are used in work its status become
				// unavailable and it cannot be deleted from db, and the status of all is
				// services become unavailable
				category.setStatus("unavailable");
				edit(category);
				serviceDao.putServicesUnavailable(category.getIdCategorie());

			} else {
				// if category have services that are not used in work it can be deleted only if
				// all its services are deleted

				// delete category services first
				boolean successfulyDeleted = serviceDao.deleteAllCategoryServices(category.getIdCategorie());

				// test if all category services have been removed
				if (successfulyDeleted) {
					// if services have been removed, category can be deleted
					deleteById(id);
				} else {
					// if a problem occurred while deleting services, category can't be deleted and
					// an exception is thrown
					// this is not needed actually because if there's any problem its going be
					// caught in edit method but i'm going to leave it just in case
					throw new DatabaseException(
							"Category have services that are not used in work it can be deleted only if all its services are deleted");
				}
			}
		} else {
			// if category have no services it can be deleted
			deleteById(id);
		}

	}

	public List<Service> categoryServicesUsedInWorkList(Categorie categorie) {
		WorkDao workDao = HibernateUtil.getWorkDao();

		// get all Work list
		ArrayList<Work> workList = workDao.listAll();
		// used services list
		ArrayList<Service> usedServicesList = new ArrayList<>();

		ArrayList<Service> categorieServicesList = new ArrayList<Service>(categorie.getServices());
		// test if category have services that are used in work, if yes put them in a
		// list
		for (int i = 0; i < workList.size(); i++) {

			for (int j = 0; j < categorieServicesList.size(); j++) {

				if (workList.get(i).getService().getName().equals(categorieServicesList.get(j).getName()) 
						&& (!usedServicesList.contains(workList.get(i).getService()))) {
					usedServicesList.add(workList.get(i).getService());
				}
			}
		}

		return usedServicesList;
	}

	@Override
	public void deleteByQuery(Categorie t) {
			// not implemented yet	
	}

	public boolean iscategoryNameChanged(Categorie category) {

		String oldCategoryName = findById(category.getIdCategorie()).getName();
		if (category.getName().equals(oldCategoryName)) {
			return false;
		}
		return true;
	}
	
	//TODO Add edit category image

	@Override
	public boolean edit(Categorie category) {

		if (category == null) {
			// throw exception Category null
			throw new DataNotFoundException("Category cannot be null! ");
		}

		// test if category dosn't exists
		if (findById(category.getIdCategorie()) == null) {
			// throw exception Category not found
			throw new DataNotFoundException("Category with id :" + category.getIdCategorie() + " not found  !");
		}

		// test if category name is empty or null
		if (category.getName().equals("") || category.getName() == null) {
			throw new EmptyParameterException("Category Name cannot be null or empty");
		}

		// test if category status is empty or null
		if (category.getStatus().equals("") || category.getStatus() == null) {
			throw new EmptyParameterException("Category Status cannot be null or empty");
		}

		// test if category status is not 'available' nor 'unavailable'
		if (!category.getStatus().equals("available") && !category.getStatus().equals("unavailable")) {
			throw new DataNotFoundException("Category Status must be either 'available' or 'unavailable'");
		}

		// test if category name exists but its the same (category has the same name)
		if (iscategoryNameChanged(category)) {
			// test if category name already exists
			int ifCategoryExistId = iscategoryNameAlreadyExists(category);
			if (ifCategoryExistId!=0) {
				throw new DataAlreadyExistsException("Category name: " + category.getName() + " already exists");
			}
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.update(category);
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException Category " + e.getMessage());
		}

	}

	@Override
	public Categorie findById(int i) {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			Categorie c = (Categorie) session.get(Categorie.class, i);
			session.getTransaction().commit();
			session.close();
			if (c == null) {
				throw new DataNotFoundException("Categorie with id : " + i + " not found !");
			}
			return c;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public ArrayList<Categorie> listAll() {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Categorie> l = (ArrayList<Categorie>) session.createQuery("from Categorie").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public ArrayList<Categorie> listAllAvailableCategories() {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Categorie> l = (ArrayList<Categorie>) session
					.createQuery("from Categorie as c where c.status = 'available'").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public ArrayList<Categorie> listAllUnAvailableCategories() {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Categorie> l = (ArrayList<Categorie>) session
					.createQuery("from Categorie as c where c.status = 'unavailable'").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

}
