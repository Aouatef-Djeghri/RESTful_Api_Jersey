package com.servicepro.services;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.servicepro.config.HibernateUtil;
import com.servicepro.dao.interfaces.*;
import com.servicepro.entities.*;


/*
 * Aouatef Djeghri
 * Year 2020
 */


@Path("/login")
public class LoginResources {



	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public User login(@FormParam("email") String email, @FormParam("password") String password) {
		UserDao userDao = HibernateUtil.getUserDao();
		return userDao.login(email, password);

	}

	// TODO try add login using one time password


	@POST
	@Path("/forgetPassword")
	@Produces(MediaType.APPLICATION_JSON)
	public Response forgetPassword(@FormParam("email") String email) {
		UserDao userDao = HibernateUtil.getUserDao();
		userDao.sendGeneratedPasswordToEmail(email);
		return Response.ok().build();

	}
	
}
