package com.servicepro.dao.interfaces;

import java.util.ArrayList;


import com.servicepro.entities.User;

public interface UserDao extends Dao<User> {
	public User login(String user, String password);

	public boolean ifEmailExists(String email);

	public boolean ifPhoneExists(String phone);


	void editPassword(int userId, String oldPassword, String newPassword);

	public boolean editProfile(User user);

	ArrayList<User> listAllByService(int serviceId, int clientId);
	
	void sendGeneratedPasswordToEmail(String email);
	
	boolean setUserVisibilityInSearch(int userId , int visibility);
}
