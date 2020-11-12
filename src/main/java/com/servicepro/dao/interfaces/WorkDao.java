package com.servicepro.dao.interfaces;

import java.util.List;

import com.servicepro.entities.Work;

public interface WorkDao extends Dao<Work> {

	public List<Work> listAllByRole(String role, int idUser);

	List<Work> listAllByRoleAndStatus(String role, int idUser, String status);

	public List<Work> listAllAvailableWork(int idUser);

	public List<Work> listAllRecommendedWork(int idUser);

	public List<Work> listAllAvailableWorkByCategory(int idCategory, int idUser);

	public List<Work> listAllAvailableWorkByService(int idService, int idUser);
	
	void cancelWork(int workId);
	
	void deleteWork(int workId);
}
