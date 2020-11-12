package com.servicepro.dao.impl;

import java.util.ArrayList;
import org.hibernate.Session;

import com.servicepro.config.CloudinaryUtil;
import com.servicepro.config.HibernateUtil;
import com.servicepro.dao.interfaces.AddressDao;
import com.servicepro.dao.interfaces.ApplicationDao;
import com.servicepro.dao.interfaces.CategorieDao;
import com.servicepro.dao.interfaces.ServiceDao;
import com.servicepro.dao.interfaces.UserDao;
import com.servicepro.dao.interfaces.WorkDao;
import com.servicepro.entities.Application;
import com.servicepro.entities.Service;
import com.servicepro.entities.User;
import com.servicepro.entities.Work;
import com.servicepro.exception.DataNotFoundException;
import com.servicepro.exception.DatabaseException;
import com.servicepro.exception.EmptyParameterException;



/*
 * Aouatef Djeghri
 * Year 2020
 */




public class WorkDaoImpl implements WorkDao {

	Session session;
	UserDao userDao = HibernateUtil.getUserDao();
	CategorieDao categorieDao = HibernateUtil.getCategorieDao();
	AddressDao addressDao = HibernateUtil.getAddressDao();
	ServiceDao serviceDao = HibernateUtil.getServiceDao();


	
	@Override
	public int add(Work work) {

		if (work == null) {
			// throw exception user not found
			throw new DataNotFoundException("Work cannot be null !");
		} else {
			if (work.getAddress() == null) {
				// throw exception user not found
				throw new DataNotFoundException("Work address cannot be null !");
			} else {
				
				if (work.getAddress().getCommune()==null) {
					work.getAddress().setCommune("");
				}
				if (work.getAddress().getWilaya()==null) {
					work.getAddress().setWilaya("");
				}
				
				if (String.valueOf(work.getAddress().getLatitude()) == null) {
					throw new DataNotFoundException("Work address Latitude cannot be null !");
				}
				if (String.valueOf(work.getAddress().getLongitude()) == null) {
					throw new DataNotFoundException("Work address Longitude cannot be null !");
				}
			}

			if (work.getService() == null) {
				throw new DataNotFoundException("Work Service cannot be null !");
			} else {
				if (String.valueOf(work.getService().getIdService()) == null) {
					throw new DataNotFoundException("Work service id cannot be null !");
				}
			}

			if (work.getUser() == null) {
				throw new DataNotFoundException("Work User cannot be null !");
			} else {
				if (String.valueOf(work.getUser().getIdUser()) == null) {
					throw new DataNotFoundException("Work user id cannot be null !");
				}
			}
		}

		if (work.getTitle() == null) {
			throw new DataNotFoundException("Work title cannot be null !");
		}
		if (work.getDescription() == null) {
			throw new DataNotFoundException("Work description cannot be null !");
		}
		if (work.getStatus() == null) {
			throw new DataNotFoundException("Work status cannot be null !");
		}
		if (work.getType() == null) {
			throw new DataNotFoundException("Work status cannot be null !");
		}
		if (work.getPaymentMethod() == null) {
			throw new DataNotFoundException("Work paymentMethod cannot be null !");
		}
		if (work.getDueDate() == null) {
			throw new DataNotFoundException("Work dueDate cannot be null !");
		}

		try {
			addressDao.add(work.getAddress());
			session = HibernateUtil.getSession();
			session.beginTransaction();
			int id = (int) session.save(work);
			session.getTransaction().commit();
			session.close();
			return id;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public int deleteById(int id) {

		if (findById(id) == null) {
			throw new DataNotFoundException("Work with id :" + id + " not found  !");
		}
		Work c = findById(id);
		String imageOneURL = c.getFirstImage();
		String imageTwoURL = c.getSeceondImage();
		String imageThreeURL = c.getThirdImage();
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.delete(c);
			session.getTransaction().commit();
			session.close();
			CloudinaryUtil.deleteImage(imageOneURL, "work");
			CloudinaryUtil.deleteImage(imageTwoURL, "work");
			CloudinaryUtil.deleteImage(imageThreeURL, "work");
			return 1;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void deleteByQuery(Work t) {
		//Not implemented yet
	}

	@Override
	public boolean edit(Work work) {

		if (work == null) {
			// throw exception user not found
			throw new DataNotFoundException("Work cannot be null! ");
		}

		if (findById(work.getIdWork()) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Work with id :" + work.getIdWork() + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.update(work);
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException Work" + e.getMessage());
		}

	}

	@Override
	public Work findById(int i) {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			Work c = (Work) session.get(Work.class, i);
			session.getTransaction().commit();
			session.close();
			if (c == null) {
				throw new DataNotFoundException("Work with id : " + i + " not found !");
			}
			return c;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public ArrayList<Work> listAll() {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Work> l = (ArrayList<Work>) session.createQuery("from Work").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	// TODO Update this method
	@Override
	public ArrayList<Work> listAllByRole(String role, int idUser) {

		if (role == null || role.equals("")) {
			throw new EmptyParameterException("First Parameter 'Role' cannot be empty or null !");
		}

		// test if user exists or not
		if (userDao.findById(idUser) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id :" + idUser + " not found  !");
		}

		String r = null;
		// test depending on the role to fetch the list
		if (role.equals("artisan")) {
			r = "select work from Application as a where a.user.idUser = " + idUser + " and a.work.user.idUser !="
					+ idUser;
		}
		if (role.equals("client")) {
			r = "from Work as a where a.user.idUser =  " + idUser;
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Work> l = (ArrayList<Work>) session.createQuery(r).list();
			session.getTransaction().commit();
			session.close();
			return l;

		} catch (Exception e) {
			// test if role is correct , request, finish, pending...
			throw new DatabaseException(
					"Parameter 'Role' is not correct, it must me either 'client' or 'artisan' or 'admin'  !");
		}

	}

	// TODO Update this method
	@Override
	public ArrayList<Work> listAllByRoleAndStatus(String role, int idUser, String status) {


		// test if user exists or not
		if (userDao.findById(idUser) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id :" + idUser + " not found  !");
		}

		//test if status is not correct
		if (!status.equals("request") && !status.equals("pending") && !status.equals("finished")) {

			throw new DatabaseException(
					"Work Parameter 'status' is not correct, it must me either 'request' or 'pending' or 'finished'  ! ");
		}

		String r = null;
		// test depending on the role to fetch the list
		if (role.equals("artisan")) {

			switch (status) {

			case "request":

				r = "select work from Application as a where a.user.idUser = " + idUser
						+ " and ( (a.status = 'request' and a.work.status = 'request') or( a.status = 'declined' and a.work.status = 'request' ) or( a.status = 'declined' and a.work.status = 'pending' ) ) and a.work.user.idUser != "
						+ idUser;

				break;

			case "pending":

				r = "select work from Application as a where a.user.idUser = " + idUser
						+ " and ( (a.status = 'accepted' and a.work.status = 'pending') or (a.status = 'accepted' and a.work.status = 'mid_finished')) and a.work.user.idUser != "
						+ idUser;
				break;

			case "finished":

				r = "select work from Application as a where a.user.idUser = " + idUser
						+ " and a.status = 'accepted' and a.work.status = 'finished' and a.work.user.idUser != " + idUser;

				break;
			default :
				break;
			}

		}
		if (role.equals("client")) {

			switch (status) {

			case "request":
				r = "from Work as a where a.user.idUser = " + idUser + " and a.status = '" + status + "'";
				break;

			case "pending":
				r = "from Work as a where a.user.idUser = " + idUser + " and ( a.status = 'pending' or a.status = 'mid_finished')";
				break;

			case "finished":
				r = "from Work as a where a.user.idUser = " + idUser + " and ( a.status = 'finished' or a.status = 'canceled')";
				break;
			default :
				break;
			}

		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Work> l = (ArrayList<Work>) session.createQuery(r).list();
			session.getTransaction().commit();
			session.close();
			return l;

		} catch (Exception e) {
			// test if role is correct , request, finish, pending...
			throw new DatabaseException(
					"Parameter 'Role' is not correct, it must me either 'client' or 'artisan' or 'admin'  ! "
							+ e.getMessage());
		}

	}

	@Override
	public ArrayList<Work> listAllAvailableWork(int idUser) {

		// test if user exists or not
		if (userDao.findById(idUser) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id :" + idUser + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			String r = "from Work as a where a.type = 'post' and a.status ='request' and a.user.idUser != " + idUser;
			@SuppressWarnings("unchecked")
			ArrayList<Work> l = (ArrayList<Work>) session.createQuery(r).list();
			session.getTransaction().commit();
			session.close();
			return l;

		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public ArrayList<Work> listAllRecommendedWork(int idUser) {
		
		
		ArrayList<Work> recommendedWorkList = new ArrayList<Work>();

		// test if user exists or not
		if (userDao.findById(idUser) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id :" + idUser + " not found  !");
		}
		User user = userDao.findById(idUser);
		ArrayList<Service> userServices = new ArrayList<Service>(user.getServices());

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			String r = "from Work as a where a.type = 'post' and a.status ='request' and a.user.idUser != " + idUser;
			@SuppressWarnings("unchecked")
			ArrayList<Work> l = (ArrayList<Work>) session.createQuery(r).list();
			session.getTransaction().commit();
			session.close();
			for (int i = 0; i < l.size(); i++) {
				for (int j = 0; j < userServices.size(); j++) {
					if (userServices.get(j).getIdService() == l.get(i).getService().getIdService()) {
						recommendedWorkList.add(l.get(i));
					}
				}
			}

			return recommendedWorkList;

		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public ArrayList<Work> listAllAvailableWorkByCategory(int idCategory, int idUser) {


		// test if user exists or not
		if (userDao.findById(idUser) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id :" + idUser + " not found  !");
		}

		// test if Category exists or not
		if (categorieDao.findById(idCategory) == null) {
			// throw exception Category not found
			throw new DataNotFoundException("Category with id :" + idCategory + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			String r = "from Work as a where a.type = 'post' and a.status ='request' and a.service.categorie.idCategorie = "
					+ idCategory + " and a.user.idUser != " + idUser;
			@SuppressWarnings("unchecked")
			ArrayList<Work> l = (ArrayList<Work>) session.createQuery(r).list();
			session.getTransaction().commit();
			session.close();
			return l;

		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public ArrayList<Work> listAllAvailableWorkByService(int idService, int idUser) {

		
		// test if user exists or not
		if (userDao.findById(idUser) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id :" + idUser + " not found  !");
		}

		// test if Category exists or not
		if (serviceDao.findById(idService) == null) {
			// throw exception Category not found
			throw new DataNotFoundException("Service with id :" + idService + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			String r = "from Work as a where a.type = 'post' and a.status ='request' and a.service.idService = "
					+ idService + " and a.user.idUser != " + idUser;
			@SuppressWarnings("unchecked")
			ArrayList<Work> l = (ArrayList<Work>) session.createQuery(r).list();
			session.getTransaction().commit();
			session.close();
			return l;

		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}
	
	@Override
	public void cancelWork(int workId) {
	
		
		if (String.valueOf(workId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("WorkId cannot be null! ");
		}
		if (findById(workId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Work with id :" + workId + " not found  !");
		}
		Work work = findById(workId);
		work.setStatus("canceled");
		edit(work);
	}
	
	@Override
	public void deleteWork(int workId) {

		if (String.valueOf(workId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("WorkId cannot be null! ");
		}
		if (findById(workId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Work with id :" + workId + " not found  !");
		}
		
		Work work = findById(workId);
		ApplicationDao applicationDao= HibernateUtil.getApplicationDao();
		ArrayList<Application> applications = new ArrayList<>(work.getApplications());
		 if (!applications.isEmpty()) {
			 for(int i = 0; i<work.getApplications().size(); i++) {
				 applicationDao.deleteById(applications.get(i).getIdApplication());
			 }
         } 
		 
		 int isDeleted = deleteById(workId);
    	 if(isDeleted == 1) {
    		 addressDao.deleteById(work.getAddress().getIdAddress());
    	 }
	}
}
