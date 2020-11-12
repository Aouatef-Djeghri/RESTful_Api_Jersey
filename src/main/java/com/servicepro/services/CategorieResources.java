package com.servicepro.services;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import com.servicepro.config.HibernateUtil;
import com.servicepro.dao.interfaces.*;
import com.servicepro.entities.*;


/*
 * Aouatef Djeghri
 * Year 2020
 */


@Path("/categories")
public class CategorieResources {


	

	@GET
	@Produces("application/json")
	public List<Categorie> getCategories() {
		CategorieDao categorieDao = HibernateUtil.getCategorieDao();
		return categorieDao.listAll();
	}
	

	@GET
	@Path("/available")
	@Produces("application/json")
	public List<Categorie> getAvailableCategories() {
		CategorieDao categorieDao = HibernateUtil.getCategorieDao();
		return categorieDao.listAllAvailableCategories();
	}

	@GET
	@Path("/unvailable")
	@Produces("application/json")
	public List<Categorie> getUnavailableCategories() {
		CategorieDao categorieDao = HibernateUtil.getCategorieDao();
		return categorieDao.listAllUnAvailableCategories();
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Categorie getCategorie(@PathParam("id") int id) {
		CategorieDao categorieDao = HibernateUtil.getCategorieDao();
		return categorieDao.findById(id);
	}

	// listAllServicesByCategorie
	@GET
	@Path("/{categoryId}/services")
	@Produces("application/json")
	public List<Service> getListServices(@PathParam("categoryId") int categoryId) {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		return serviceDao.listAllServicesByCategorie(categoryId);
	}

	@GET
	@Path("/{categoryId}/availableServices")
	@Produces("application/json")
	public List<Service> getListAvailableServices(@PathParam("categoryId") int categoryId) {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		return serviceDao.listAllAvailableServicesByCategorie(categoryId);
	}

	@GET
	@Path("/{categoryId}/unavailableServices")
	@Produces("application/json")
	public List<Service> getListUnavailableServices(@PathParam("categoryId") int categoryId) {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		return serviceDao.listAllUnavailableServicesByCategorie(categoryId);
	}

	@POST
	@Path("/create")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addCategorie(Categorie categorie) {
		CategorieDao categorieDao = HibernateUtil.getCategorieDao();
		categorieDao.add(categorie);
		return Response.ok().build();
	}

	@PUT
	@Path("/update")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateCategorie(Categorie categorie) { // @PathParam("id") int id,
		CategorieDao categorieDao = HibernateUtil.getCategorieDao();
		categorieDao.edit(categorie);
		return Response.ok().build();
	}

	@DELETE
	@Path("/delete/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteCategorie(@PathParam("id") int id) {
		CategorieDao categorieDao = HibernateUtil.getCategorieDao();
		categorieDao.deleteCategory(id);
		return Response.ok().build();
	}

}
