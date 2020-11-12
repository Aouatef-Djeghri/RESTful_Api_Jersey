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
import com.servicepro.dao.interfaces.*;
import com.servicepro.entities.*;


/*
 * Aouatef Djeghri
 * Year 2020
 */


@Path("/services")
public class ServiceResources {


	@GET
	@Produces("application/json")
	public List<Service> getServices() {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		return serviceDao.listAll();
	}

	@GET
	@Path("/available")
	@Produces("application/json")
	public List<Service> getAvailableServices() {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		return serviceDao.listAllAvailableServices();
	}

	@GET
	@Path("/unavailable")
	@Produces("application/json")
	public List<Service> getUnavailableServices() {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		return serviceDao.listAllUnavailableServices();
	}

	@GET
	@Path("/user/{userId}")
	@Consumes("application/json")
	@Produces("application/json")
	public List<Service> getServicesArtisan(@PathParam("userId") int userId) {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		return serviceDao.getServicesArtisan(userId);
	}

	@PUT
	@Path("/user/{userId}/update")
	@Consumes("application/json")
	@Produces("application/json")
	public List<Service> updateServicesArtisan(@PathParam("userId") int userId, List<Service> services) {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		return serviceDao.updateServicesList(userId, services);
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Service getService(@PathParam("id") int id) {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		return serviceDao.findById(id);

	}

	@POST
	@Path("/create")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addService(Service service) {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		serviceDao.add(service);
		return Response.ok().build();
	}

	@PUT
	@Path("/update")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateService(Service service) { // @PathParam("id") int id,
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		serviceDao.edit(service);
		return Response.ok().build();
	}

	@DELETE
	@Path("/delete/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteService(@PathParam("id") int id) {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		serviceDao.deleteService(id);
		return Response.ok().build();
	}

}