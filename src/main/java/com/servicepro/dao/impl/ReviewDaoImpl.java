package com.servicepro.dao.impl;

import java.util.ArrayList;
import org.hibernate.Session;

import com.servicepro.config.HibernateUtil;
import com.servicepro.dao.interfaces.ReviewDao;
import com.servicepro.dao.interfaces.UserDao;
import com.servicepro.dao.interfaces.WorkDao;
import com.servicepro.entities.Review;
import com.servicepro.exception.DataNotFoundException;
import com.servicepro.exception.DatabaseException;
import com.servicepro.exception.EmptyParameterException;



/*
 * Aouatef Djeghri
 * Year 2020
 */



public class ReviewDaoImpl implements ReviewDao {

	Session session;

	WorkDao workDao = HibernateUtil.getWorkDao();
	UserDao userDao = HibernateUtil.getUserDao();
	
	@Override
	public int add(Review review) {


		
		if (review == null) {
			// throw exception Review not found
			throw new DataNotFoundException("Review cannot be null! ");
		}

		if (review.getWork() == null) {
			// throw exception Review not found
			throw new DataNotFoundException("Review work cannot be null! ");
		}

		if (review.getUser() == null) {
			// throw exception Review not found
			throw new DataNotFoundException("Review user cannot be null! ");
		}

		// test if work is empty or null
		if (workDao.findById(review.getWork().getIdWork()) == null) {
			// throw exception Work not found
			throw new DataNotFoundException("Review Work with id :" + review.getWork().getIdWork() + " not found  !");
		}
		// test if User is empty or null
		if (userDao.findById(review.getUser().getIdUser()) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Reviewer User with id :" + review.getUser().getIdUser() + " not found  !");
		}
		// test if review title is empty or null
		if (review.getTitle().equals("") || review.getTitle() == null) {
			throw new EmptyParameterException("Review title cannot be null or empty");
		}
		// test if review body is empty or null
		if (review.getBody().equals("") || review.getBody() == null) {
			throw new EmptyParameterException("Review body cannot be null or empty");
		}
		// test if review date is null
		if (review.getReviewDate() == null) {
			throw new EmptyParameterException("Review date cannot be null or empty");
		}
		// test if review rating is empty or null
		if (String.valueOf(review.getRating()) == null) {
			throw new EmptyParameterException("Review rating cannot be null or empty");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			int id = (int) session.save(review);
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
			// throw exception Review not found
			throw new DataNotFoundException("Review with id :" + id + " not found  !");
		}
		Review review = findById(id);
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.delete(review);
			session.getTransaction().commit();
			session.close();
			return 1;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public void deleteByQuery(Review t) {
		// Not implemented yet
	}

	@Override
	public boolean edit(Review review) {

		
		if (review == null) {
			// throw exception Review not found
			throw new DataNotFoundException("Review cannot be null! ");
		}

		// test if Review dosn't exists
		if (findById(review.getIdReview()) == null) {
			// throw exception uReviewser not found
			throw new DataNotFoundException("Review with id :" + review.getIdReview() + " not found  !");
		}
		if (review.getWork() == null) {
			// throw exception Review not found
			throw new DataNotFoundException("Review Work cannot be null! ");
		}
		if (review.getUser() == null) {
			// throw exception Review not found
			throw new DataNotFoundException("Review User cannot be null! ");
		}

		// test if work is empty or null
		if (workDao.findById(review.getWork().getIdWork()) == null) {
			// throw exception Work not found
			throw new DataNotFoundException("Review Work with id :" + review.getWork().getIdWork() + " not found  !");
		}
		// test if User is empty or null
		if (userDao.findById(review.getUser().getIdUser()) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Reviewer User with id :" + review.getUser().getIdUser() + " not found  !");
		}
		// test if review title is empty or null
		if (review.getTitle().equals("") || review.getTitle() == null) {
			throw new EmptyParameterException("Review title cannot be null or empty");
		}
		// test if review body is empty or null
		if (review.getBody().equals("") || review.getBody() == null) {
			throw new EmptyParameterException("Review body cannot be null or empty");
		}
		// test if review date is null
		if (review.getReviewDate() == null) {
			throw new EmptyParameterException("Review date cannot be null or empty");
		}
		// test if review rating is empty or null
		if (String.valueOf(review.getRating()) == null) {
			throw new EmptyParameterException("Review rating cannot be null or empty");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.update(review);
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException Service" + e.getMessage());
		}
	}

	@Override
	public Review findById(int i) {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			Review c = (Review) session.get(Review.class, i);
			session.getTransaction().commit();
			session.close();
			if (c == null) {
				// throw exception Review not found
				throw new DataNotFoundException("Review with id : " + i + " not found  !");
			}
			return c;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public ArrayList<Review> listAll() {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Review> l = (ArrayList<Review>) session.createQuery("from Review").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public ArrayList<Review> getWorkReviews(int workId) {

		
		// test if Work is empty or null
		if (workDao.findById(workId) == null) {
			// throw exception Work not found
			throw new DataNotFoundException("Reviewer Work with id :" + workId + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Review> l = (ArrayList<Review>) session
					.createQuery("from Review as r where r.work.idWork = " + workId).list();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	// to get this list of all the reviews that the user has made about others as a client or as an artisan 
	@Override
	public ArrayList<Review> listUserReviewsAsReviewer(int userId) {

		
		// test if User is empty or null
		if (userDao.findById(userId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Reviewer User with id :" + userId + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Review> l = (ArrayList<Review>) session
					.createQuery("from Review as r where r.user.idUser = " + userId).list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	// to get this list of all the reviews that the others have made about this user
	@Override
	public ArrayList<Review> listUserReviewsAsReviewee(int userId) {

		
		// test if User is empty or null
		if (userDao.findById(userId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Reviewer User with id :" + userId + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Review> l = (ArrayList<Review>) session.createQuery("from Review as r where r.user.idUser != "
					+ userId + " and ( (r.work.user.idUser = " + userId + ") or (" + userId
					+ " in (Select user.idUser from Application as a where a.status = 'accepted' and a.user.idUser ="
					+ userId + " and a.work.idWork = r.work.idWork)) )").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	// to get this list of reviews made about the user as a client
	@Override
	public ArrayList<Review> listUserReviewsAsAClientReviewee(int clientId) {

		
		// test if User is empty or null
		if (userDao.findById(clientId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Reviewer User with id :" + clientId + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Review> l = (ArrayList<Review>) session.createQuery(
					"from Review as r where r.user.idUser != " + clientId + " and r.work.user.idUser = " + clientId)
					.list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	// to get this list of reviews made about the user as an artisan
	@Override
	public ArrayList<Review> listUserReviewsAsArtisanAsReviewee(int artisanId) {
		
		// test if User is empty or null
		if (userDao.findById(artisanId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Reviewer User with id :" + artisanId + " not found  !");
		}
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Review> l = (ArrayList<Review>) session.createQuery("from Review as r where r.user.idUser != "
					+ artisanId + " and r.work.user.idUser != " + artisanId + " and " + artisanId
					+ " in (Select user.idUser from Application as a where a.status = 'accepted' and a.user.idUser ="
					+ artisanId + " and a.work.idWork = r.work.idWork)").list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	// to get this list of reviews that the user made as a client
	@Override
	public ArrayList<Review> listUserReviewsAsClientAsReviewer(int clientId) {

		
		// test if User is empty or null
		if (userDao.findById(clientId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Reviewer User with id :" + clientId + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Review> l = (ArrayList<Review>) session.createQuery(
					"from Review as r where r.user.idUser = " + clientId + " and r.work.user.idUser = " + clientId)
					.list();
			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	// to get this list of reviews that the user made as an artisan
	@Override
	public ArrayList<Review> listUserReviewsAsArtisanAsReviewer(int artisanId) {

		// test if User is empty or null
		if (userDao.findById(artisanId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("Reviewer User with id :" + artisanId + " not found  !");
		}

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<Review> l = (ArrayList<Review>) session.createQuery(
					"from Review as r where r.user.idUser = " + artisanId + " and r.work.user.idUser != " + artisanId)
					.list();

			session.getTransaction().commit();
			session.close();
			return l;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

}
