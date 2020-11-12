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
import com.servicepro.dao.interfaces.ApplicationDao;
import com.servicepro.dao.interfaces.WorkDao;
import com.servicepro.entities.Application;
import com.servicepro.entities.Work;


/*
 * Aouatef Djeghri
 * Year 2020
 */


@Path("/works")
public class WorkResources {

	@GET
	@Path("/{role}/{userId}")
	@Produces("application/json")
	public List<Work> getWorkList(@PathParam("role") String role, @PathParam("userId") int userId) {

		WorkDao workDao = HibernateUtil.getWorkDao();
		return workDao.listAllByRole(role, userId);
	}

	@GET
	@Path("/{role}/{userId}/{status}")
	@Produces("application/json")
	public List<Work> getWorkList(@PathParam("role") String role, @PathParam("userId") int userId,
			@PathParam("status") String status) {

		WorkDao workDao = HibernateUtil.getWorkDao();
		return workDao.listAllByRoleAndStatus(role, userId, status);
	}

	@GET
	@Path("/posts/{userId}")
	@Produces("application/json")
	public List<Work> listAllAvailableWork(@PathParam("userId") int userId) {

		WorkDao workDao = HibernateUtil.getWorkDao();
		return workDao.listAllAvailableWork(userId);
	}

	@GET
	@Path("/posts/recommendation/user/{userId}")
	@Produces("application/json")
	public List<Work> listAllRecommendedWork(@PathParam("userId") int userId) {

		WorkDao workDao = HibernateUtil.getWorkDao();
		return workDao.listAllRecommendedWork(userId);
	}

	@GET
	@Path("/posts/recommendation/category/{categoryId}/{userId}")
	@Produces("application/json")
	public List<Work> listAllAvailableWorkByCategory(@PathParam("categoryId") int categoryId,
			@PathParam("userId") int userId) {

		WorkDao workDao = HibernateUtil.getWorkDao();
		return workDao.listAllAvailableWorkByCategory(categoryId, userId);
	}

	@GET
	@Path("/posts/recommendation/service/{serviceId}/{userId}")
	@Produces("application/json")
	public List<Work> listAllAvailableWorkByService(@PathParam("serviceId") int serviceId,
			@PathParam("userId") int userId) {

		WorkDao workDao = HibernateUtil.getWorkDao();
		return workDao.listAllAvailableWorkByService(serviceId, userId);
	}

	@POST
	@Path("/createWorkPost")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createWorkPost(Work work) {
		WorkDao workDao = HibernateUtil.getWorkDao();
		workDao.add(work);
		return Response.ok().build();
	}

	@POST
	@Path("/createWorkProposal")
	@Consumes("application/json")
	@Produces("application/json")
	public Response createWorkProposal(Application application) {
		ApplicationDao applicationDao = HibernateUtil.getApplicationDao();
		applicationDao.addWorkApplication(application);
		return Response.ok().build();
	}

	@POST
	@Path("/acceptWorkProposal")
	@Consumes("application/json")
	@Produces("application/json")
	public Response acceptWorkProposal(Application application) {
		ApplicationDao applicationDao = HibernateUtil.getApplicationDao();
		applicationDao.acceptWorkProposal(application);
		return Response.ok().build();
	}

	@POST
	@Path("/declineWorkProposal")
	@Consumes("application/json")
	@Produces("application/json")
	public Response declineWorkProposal(Application application) {
		ApplicationDao applicationDao = HibernateUtil.getApplicationDao();
		applicationDao.declineWorkProposal(application);
		return Response.ok().build();
	}

	@POST
	@Path("/finishWork")
	@Consumes("application/json")
	@Produces("application/json")
	public Response finishWork(Application application) {
		ApplicationDao applicationDao = HibernateUtil.getApplicationDao();
		applicationDao.finishWork(application);
		return Response.ok().build();
	}

	@DELETE
	@Path("/deleteWork/{workId}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteWork(@PathParam("workId") int workId) {
		WorkDao workDao = HibernateUtil.getWorkDao();
		workDao.deleteWork(workId);
		return Response.ok().build();
	}

	@PUT
	@Path("/cancelWork/{workId}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response cancelWork(@PathParam("workId") int workId) {
		WorkDao workDao = HibernateUtil.getWorkDao();
		workDao.cancelWork(workId);
		return Response.ok().build();
	}

}
