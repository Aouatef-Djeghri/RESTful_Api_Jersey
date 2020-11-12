package com.servicepro.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.servicepro.config.HibernateUtil;
import com.servicepro.dao.interfaces.ApplicationDao;
import com.servicepro.entities.Application;


/*
 * Aouatef Djeghri
 * Year 2020
 */


@Path("/applications")
public class ApplicationRecources {

	
	
	@GET
	@Produces("application/json")
	public List<Application> getApplications() {
		ApplicationDao applicationDao = HibernateUtil.getApplicationDao();
		return applicationDao.listAll();
	}
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Application getApplication(@PathParam("id") int id) {
		ApplicationDao applicationDao = HibernateUtil.getApplicationDao();
		return applicationDao.findById(id);
	}
	
	
	@POST
	@Path("/createApplication")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createApplication(Application application) {
		ApplicationDao applicationDao = HibernateUtil.getApplicationDao();
		applicationDao.add(application);
		return Response.ok().build();
	}
	
	
	@DELETE
	@Path("/deleteApplication/{applicationId}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteApplication(@PathParam("applicationId") int applicationId) {
		ApplicationDao applicationDao = HibernateUtil.getApplicationDao();
		applicationDao.deleteApplication(applicationId);
		return Response.ok().build();
	}
	
}
