package com.servicepro.dao.interfaces;

import com.servicepro.entities.Application;

public interface ApplicationDao extends Dao<Application> {

	void addWorkApplication(Application application);
	void acceptWorkProposal(Application application);
	void declineWorkProposal(Application application);
	void finishWork(Application application);
	void deleteApplication(int applicationId);
}
