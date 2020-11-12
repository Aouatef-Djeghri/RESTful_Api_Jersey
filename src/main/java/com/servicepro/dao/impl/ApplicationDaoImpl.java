package com.servicepro.dao.impl;

import java.util.ArrayList;
import org.hibernate.Session;

import com.servicepro.config.HibernateUtil;
import com.servicepro.dao.interfaces.ApplicationDao;
import com.servicepro.dao.interfaces.UserDao;
import com.servicepro.dao.interfaces.WorkDao;
import com.servicepro.entities.Application;
import com.servicepro.entities.Work;
import com.servicepro.exception.DataNotFoundException;
import com.servicepro.exception.DatabaseException;



/*
 * Aouatef Djeghri
 * Year 2020
 */



public class ApplicationDaoImpl implements ApplicationDao {

	Session session;

	UserDao userDao = HibernateUtil.getUserDao();
	WorkDao workDao = HibernateUtil.getWorkDao();


	@Override
	public int add(Application application) {


		
		if (application == null) {
			throw new DataNotFoundException("Application cannot be null !");
		}
		
		if (application.getWork() == null) {
			throw new DataNotFoundException("Application Work cannot be null !");
		}
		
		if (application.getUser() == null) {
			throw new DataNotFoundException("Application cannot be null !");
		}
		
		
		if (workDao.findById(application.getWork().getIdWork()) == null) {
			// throw exception user not found
			throw new DataNotFoundException(
					"Application Work with id :" + application.getWork().getIdWork() + " not found  !");
		}
		if (userDao.findById(application.getUser().getIdUser()) == null) {
			// throw exception user not found
			throw new DataNotFoundException(
					"Application User with id :" + application.getUser().getIdUser() + " not found  !");
		}
		if (application.getStatus() == null) {
			throw new DataNotFoundException("Application attribute status cannot be null !");
		}
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			int id = (int) session.save(application);
			session.getTransaction().commit();
			session.close();
			return id;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException Application" + e.getMessage());
		}

	}

	@Override
	public void addWorkApplication(Application application) {


		
		if (application == null) {
			throw new DataNotFoundException("Application cannot be null !");
		} else {

			if (application.getWork() == null) {
				throw new DataNotFoundException("Application attribute Work cannot be null !");
			} else {

				if (application.getWork().getAddress() == null) {
					throw new DataNotFoundException("Work address cannot be null !");
				} else {
					if (application.getWork().getAddress().getCommune()==null) {
						application.getWork().getAddress().setCommune("");
					}
					if (application.getWork().getAddress().getWilaya()==null) {
						application.getWork().getAddress().setWilaya("");
					}
					
					if (String.valueOf(application.getWork().getAddress().getLatitude()) == null) {
						throw new DataNotFoundException("Work address Latitude cannot be null !");
					}
					if (String.valueOf(application.getWork().getAddress().getLongitude()) == null) {
						throw new DataNotFoundException("Work address Longitude cannot be null !");
					}
				}

				if (application.getWork().getService() == null) {
					throw new DataNotFoundException("Work Service cannot be null !");
				} else {
					if (String.valueOf(application.getWork().getService().getIdService()) == null) {
						throw new DataNotFoundException("Work service id cannot be null !");
					}
				}

				if (application.getWork().getUser() == null) {
					throw new DataNotFoundException("Work User cannot be null !");
				} else {
					if (String.valueOf(application.getWork().getUser().getIdUser()) == null) {
						throw new DataNotFoundException("Work user id cannot be null !");
					}
				}
				
				
			}

			if (application.getUser() == null) {
				throw new DataNotFoundException("Application attribute User cannot be null !");
			} else {
				if (String.valueOf(application.getUser().getIdUser()) == null) {
					throw new DataNotFoundException("Application user id cannot be null !");
				}
			}

		}

		workDao.add(application.getWork());
		add(application);

	}

	@Override
	public int deleteById(int id) {

		if (findById(id) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Application with id :" + id + " not found  !");
		}
		try {
			Application c = findById(id);
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.delete(c);
			session.getTransaction().commit();
			session.close();
			return 1;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException Application" + e.getMessage());
		}

	}

	@Override
	public void deleteByQuery(Application t) {
		// Not implemented yet
	}

	@Override
	public boolean edit(Application application) {

		if (application == null) {
			// throw exception user not found
			throw new DataNotFoundException("Application cannot be null! ");
		}
	
		if (findById(application.getIdApplication()) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Application with id :" + application.getIdApplication() + " not found  !");
		}
	
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.update(application);
			session.getTransaction().commit();
			session.close();

			return true;
		} catch (Exception e) {
	
			throw new DatabaseException("DatabaseException Application" + e.getMessage());
		}

	}

	@Override
	public Application findById(int i) {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			Application c = (Application) session.get(Application.class, i);
			session.getTransaction().commit();
			session.close();
			if (c == null) {
				// throw exception Application not found
				throw new DataNotFoundException("Application with id : " + i + " not found  !");
			}
			return c;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public ArrayList<Application> listAll() {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Application> l = (ArrayList<Application>) session.createQuery("from Application").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void acceptWorkProposal(Application application) {
		
		
		if (application == null) {
			// throw exception user not found
			throw new DataNotFoundException("Application cannot be null! ");
		}
		if (findById(application.getIdApplication()) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Application with id :" + application.getIdApplication() + " not found  !");
		}
		
		Application app = findById(application.getIdApplication());
		app.setStatus("accepted");
		edit(app);

		Work work = workDao.findById(app.getWork().getIdWork());
		work.setStatus("pending");
		workDao.edit(work);

		//if work type is post or proposal
		if(app.getWork().getType().equals("post")) {
			
			ArrayList<Application> applicationList = new ArrayList<>(work.getApplications()) ;
			for(int i=0; i<applicationList.size();i++) {
				
				if(applicationList.get(i).getIdApplication()!=app.getIdApplication()) {
					Application appToDecline = findById(applicationList.get(i).getIdApplication());
					appToDecline.setStatus("declined");
					edit(appToDecline);
				}
			}
		}



	}

	@Override
	public void declineWorkProposal(Application application) {
		if (application == null) {
			// throw exception user not found
			throw new DataNotFoundException("Application cannot be null! ");
		}
		if (findById(application.getIdApplication()) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Application with id :" + application.getIdApplication() + " not found  !");
		}
		
		Application app = findById(application.getIdApplication());
		app.setStatus("declined");
		edit(app);

	}
	
	

	@Override
	public void finishWork(Application application) {
		
		
		if (application == null) {
			// throw exception user not found
			throw new DataNotFoundException("Application cannot be null! ");
		}
		if (findById(application.getIdApplication()) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Application with id :" + application.getIdApplication() + " not found  !");
		}
		
		Application app = findById(application.getIdApplication());
		Work work = workDao.findById(app.getWork().getIdWork());
		
		switch(app.getWork().getStatus()) {
		
		case "pending":
			work.setStatus("mid_finished");
			workDao.edit(work);
			break;
			
		case "mid_finished":
			work.setStatus("finished");
			workDao.edit(work);
			break;
		default :
			break;
		}
	}
	

	
	@Override
	public void deleteApplication(int applicationId) {
		if (String.valueOf(applicationId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("applicationId cannot be null! ");
		}
		if (findById(applicationId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Application with id :" + applicationId + " not found  !");
		}
		
		Application application = findById(applicationId);
		deleteById(application.getIdApplication());
	}
	
}
