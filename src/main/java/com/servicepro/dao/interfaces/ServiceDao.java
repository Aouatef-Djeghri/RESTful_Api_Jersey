package com.servicepro.dao.interfaces;

import java.util.List;

import com.servicepro.entities.Service;

public interface ServiceDao extends Dao<Service> {

	public List<Service> listAllAvailableServices();

	public List<Service> listAllUnavailableServices();

	public List<Service> listAllServicesByCategorie(int categorieId);

	public List<Service> listAllAvailableServicesByCategorie(int categorieId);

	public List<Service> listAllUnavailableServicesByCategorie(int categorieId);

	public List<Service> getServicesArtisan(int idArtisan);

	public List<Service> updateServicesList(int idUser, List<Service> services);

	boolean deleteAllCategoryServices(int categorieId);

	public void putServicesUnavailable(int categorieId);

	void deleteService(int id);
}
