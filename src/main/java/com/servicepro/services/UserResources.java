package com.servicepro.services;

import java.util.List;

import javax.ws.rs.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.servicepro.config.HibernateUtil;
import com.servicepro.dao.interfaces.*;
import com.servicepro.entities.*;
import com.servicepro.responseModel.ErrorMessage;


/*
 * Aouatef Djeghri
 * Year 2020
 */


@Path("/users")
public class UserResources {


	@POST
	@Path("/createUser")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createUser(User user) {
		UserDao userDao = HibernateUtil.getUserDao();
		int isCreated = userDao.add(user);
		if (isCreated != -1) {
			return Response.status(Response.Status.CREATED).build();
		} else {
			ErrorMessage errorMessage = new ErrorMessage("Server Error : User was not created", 500,
					"http://lilac-software-solutions.com");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
		}
	}

	@GET
	@Produces("application/json")
	public List<User> getUsers() {
		UserDao userDao = HibernateUtil.getUserDao();
		return userDao.listAll();
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public User getUser(@PathParam("id") int id) {
		UserDao userDao = HibernateUtil.getUserDao();
		return userDao.findById(id);
	}

	@POST
	@Path("/updateUser")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateUser(User user) {
		UserDao userDao = HibernateUtil.getUserDao();
		boolean isUptaed = userDao.editProfile(user);
		if (isUptaed) {
			return Response.ok().build();
		} else {
			ErrorMessage errorMessage = new ErrorMessage("Server Error : User was not updated", 500,
					"http://lilac-software-solutions.com");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
		}

	}

	@POST
	@Path("/updatePassword")
	@Produces("application/json")
	public Response updatePassword(@FormParam("userId") int userId, @FormParam("oldPassword") String oldPassword,
			@FormParam("newPassword") String newPassword) {
		UserDao userDao = HibernateUtil.getUserDao();
		userDao.editPassword(userId, oldPassword, newPassword);
		return Response.ok().build();

	}

	@POST
	@Path("/findArtisans")
	@Produces("application/json")
	public List<User> listAllByService(@FormParam("serviceId") int serviceId,
			@FormParam("clientId") int clientId) {
		UserDao userDao = HibernateUtil.getUserDao();
		return userDao.listAllByService(serviceId, clientId);
	}
	

	@PUT
	@Path("/{userId}/visibility/{visibility}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response setUserVisibilityInSearch(@PathParam("userId") int userId, @PathParam("visibility") int visibility) {
		UserDao userDao = HibernateUtil.getUserDao();
		userDao.setUserVisibilityInSearch(userId,visibility);
		return Response.ok().build();
	}

}
