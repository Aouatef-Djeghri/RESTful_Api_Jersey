package com.servicepro.services;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.servicepro.config.HibernateUtil;
import com.servicepro.dao.interfaces.ReviewDao;
import com.servicepro.entities.Review;


/*
 * Aouatef Djeghri
 * Year 2020
 */


@Path("/reviews")
public class ReviewResources {



	// get the list of all the reviews
	@GET
	@Produces("application/json")
	public List<Review> getReviews() {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		return reviewDao.listAll();
	}

	// get review by its id
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Review getReview(@PathParam("id") int id) {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		return reviewDao.findById(id);
	}

	// get work reviews by work id
	@GET
	@Path("/work/{workId}")
	@Produces("application/json")
	public List<Review> getWorkReviews(@PathParam("workId") int workId) {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		return reviewDao.getWorkReviews(workId);
	}

	// to get the list of all the reviews that the user has made about others
	@GET
	@Path("/user/{userId}/reviewer")
	@Produces("application/json")
	public List<Review> listUserReviewsAsReviewer(@PathParam("userId") int userId) {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		return reviewDao.listUserReviewsAsReviewer(userId);
	}

	// to get the list of all the reviews that the others have made about this user
	@GET
	@Path("/user/{userId}/reviewee")
	@Produces("application/json")
	public List<Review> listUserReviewsAsReviewee(@PathParam("userId") int userId) {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		return reviewDao.listUserReviewsAsReviewee(userId); 
	}
	 
	// to get the list of reviews made about the user as a client
	@GET
	@Path("/user/{clientId}/client/reviewee")
	@Produces("application/json")
	public List<Review> listUserReviewsAsAClientReviewee(@PathParam("clientId") int clientId) {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		return reviewDao.listUserReviewsAsAClientReviewee(clientId);
	}

	// to get the list of reviews made about the user as a artisan
	@GET
	@Path("/user/{artisanId}/artisan/reviewee")
	@Produces("application/json")
	public List<Review> listUserReviewsAsArtisanAsReviewee(@PathParam("artisanId") int artisanId) {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		return reviewDao.listUserReviewsAsArtisanAsReviewee(artisanId);
	}

	// to get the list of reviews that the user made as a client
	@GET
	@Path("/user/{clientId}/client/reviewer")
	@Produces("application/json")
	public List<Review> listUserReviewsAsClientAsReviewer(@PathParam("clientId") int clientId) {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		return reviewDao.listUserReviewsAsClientAsReviewer(clientId);
	}

	// to get the list of reviews that the user made as an artisan
	@GET
	@Path("/user/{artisanId}/artisan/reviewer")
	@Produces("application/json")
	public List<Review> listUserReviewsAsArtisanAsReviewer(@PathParam("artisanId") int artisanId) {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		return reviewDao.listUserReviewsAsArtisanAsReviewer(artisanId);
	}

	@POST
	@Path("/create")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addReview(Review review) {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		reviewDao.add(review);
		return Response.ok().build();
	}

	@PUT
	@Path("/update")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateReview(Review review) {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		reviewDao.edit(review);
		return Response.ok().build();
	}

	@DELETE
	@Path("/delete/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deletereview(@PathParam("id") int id) {
		ReviewDao reviewDao = HibernateUtil.getReviewDao();
		reviewDao.deleteById(id);
		return Response.ok().build();
	}

}
