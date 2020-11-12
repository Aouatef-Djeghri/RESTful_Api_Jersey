package com.servicepro.dao.impl;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;

import com.servicepro.config.CloudinaryUtil;
import com.servicepro.config.HibernateUtil;
import com.servicepro.config.Util;
import com.servicepro.dao.interfaces.AddressDao;
import com.servicepro.dao.interfaces.ServiceDao;
import com.servicepro.dao.interfaces.UserDao;
import com.servicepro.entities.Address;
import com.servicepro.entities.User;
import com.servicepro.exception.DataAlreadyExistsException;
import com.servicepro.exception.DataNotFoundException;
import com.servicepro.exception.DatabaseException;
import com.servicepro.exception.EmptyParameterException;



/*
 * Aouatef Djeghri
 * Year 2020
 */



public class UserDaoImpl implements UserDao {
	Session session;

	AddressDao addressDao = HibernateUtil.getAddressDao();

	
	@Override
	public int add(User u) {


		
		int id = -1;

		if (u == null) {
			throw new EmptyParameterException("User Cannot be null !");
		}

		// Test if parameters ares empty or null
		String isNull = "";
		String isEmpty = "";

		// TODO optimize this test and put it in a method
		for (Field field : u.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				if (field.get(u) == null && (!field.getName().equals("serialVersionUID") && !field.getName().equals("idUser")
						&& !field.getName().equals("avatar") && !field.getName().equals("accountActivation")
						&& !field.getName().equals("aboutMe") && !field.getName().equals("isActive")
						&& !field.getName().equals("works") && !field.getName().equals("services")
						&& !field.getName().equals("applications") && !field.getName().equals("joinDate")
						&& !field.getName().equals("salt") && !field.getName().equals("reviews")) ) {
					isNull = isNull + " " + field.getName();
				}

				if (field.get(u).equals("") && (!field.getName().equals("serialVersionUID") && !field.getName().equals("idUser")
						&& !field.getName().equals("avatar") && !field.getName().equals("accountActivation")
						&& !field.getName().equals("aboutMe") && !field.getName().equals("isActive")
						&& !field.getName().equals("works") && !field.getName().equals("services")
						&& !field.getName().equals("applications") && !field.getName().equals("joinDate")
						&& !field.getName().equals("salt") && !field.getName().equals("reviews"))) {
					isEmpty = isEmpty + " " + field.getName();
				}
			} catch (Exception e) {
				// Catch Exception
			}
		}

		// TODO optimize this test and put it in a method
		if (u.getAddress() == null) {
			throw new EmptyParameterException("Parameters : Address Cannot be null !");
		}
		// TODO optimize this test and put it in a method
		for (Field field : u.getAddress().getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				if (field.get(u.getAddress()) == null && (!field.getName().equals("serialVersionUID") && !field.getName().equals("idAddress")
						&& !field.getName().equals("works") && !field.getName().equals("users"))) {
					isNull = isNull + " " + field.getName();
				}

				if (field.get(u.getAddress()).equals("") && (!field.getName().equals("serialVersionUID") && !field.getName().equals("idAddress")
						&& !field.getName().equals("works") && !field.getName().equals("users"))) {
					isEmpty = isEmpty + " " + field.getName();
				}
			} catch (Exception e) {
			
			}
		}

		if (!isNull.isEmpty() && !isEmpty.isEmpty()) {
			throw new EmptyParameterException(
					"Parameters : " + isNull + " Cannot be null !\nParameters : " + isEmpty + " Cannot be empty !");
		} else if (!isNull.isEmpty()) {
			throw new EmptyParameterException("Parameters : " + isNull + " Cannot be null !");
		} else if (!isEmpty.isEmpty()) {
			throw new EmptyParameterException("Parameters : " + isEmpty + " Cannot be empty !");
		} else if (isNull.isEmpty() && isEmpty.isEmpty()) {

			// If All the parameters are not null and not empty
			boolean isEmailExists = ifEmailExists(u.getEmail());
			boolean isPhoneExists = ifPhoneExists(u.getPhone());
			// Test if email and phone already exist
			if (!isEmailExists && !isPhoneExists) {
				addressDao.add(u.getAddress());
				u.setJoinDate(Calendar.getInstance().getTime());
				try {
					byte[] salt = createSalte();
					String newPass = generatedHash(u.getPassword(), salt);
					u.setSalt(salt);
					u.setPassword(newPass);
					u.setIsActive((byte) 1);
					session = HibernateUtil.getSession();
					session.beginTransaction();
					id = (int) session.save(u);
					session.getTransaction().commit();
					session.close();
				} catch (Exception e) {
					if (id == -1) {
						addressDao.deleteById(u.getAddress().getIdAddress());
					}
					throw new DatabaseException("Database Exception!\n" + e.getMessage());
				}

			} else if (isEmailExists && isPhoneExists) {
				throw new DataAlreadyExistsException("User Phone and Email already exit !");
			} else if (isEmailExists) {
				throw new DataAlreadyExistsException("User Email already exits !");
			} else if (isPhoneExists) {
				throw new DataAlreadyExistsException("User Phone already exits !");
			}
		}
		return id;
	}

	//TODO Add delete user
	
	@Override
	public int deleteById(int id) {

		if (findById(id) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id :" + id + " not found  !");
		}
		User c = findById(id);
		String avatarURL = c.getAvatar();
		String idImageURL = c.getIdentityImage();
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.delete(c);
			session.getTransaction().commit();
			session.close();
			CloudinaryUtil.deleteImage(avatarURL, "avatar");
			CloudinaryUtil.deleteImage(idImageURL, "ids");
			return 1;
		} catch (Exception e) {

			throw new DatabaseException("DatabaseException User " + e.getMessage());
		}

	}

	@Override
	public void deleteByQuery(User t) {
		// Not implemented yet
	}

	@Override
	public boolean edit(User user) {
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.update(user);
			session.getTransaction().commit();
			session.close();

			return true;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException User " + e.getMessage());
		}
	}

	@Override
	public boolean editProfile(User user) {

		
		if (user == null) {
			// throw exception user not found
			throw new DataNotFoundException("User cannot be null  ! ");
		}

		// test if user exists or not
		if (findById(user.getIdUser()) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id :" + user.getIdUser() + " not found  !");
		}

		String isEmptyParams = userHaveEmptyParameters(user);
		String isNullParams = userHaveNullParameters(user);
		if (!isNullParams.isEmpty() && !isEmptyParams.isEmpty()) {
			throw new EmptyParameterException("Parameters : " + isNullParams + " Cannot be null !\nParameters : "
					+ isEmptyParams + " Cannot be empty !");
		} else if (!isNullParams.isEmpty()) {
			throw new EmptyParameterException("Parameters : " + isNullParams + " Cannot be null !");
		} else if (!isEmptyParams.isEmpty()) {
			throw new EmptyParameterException("Parameters : " + isEmptyParams + " Cannot be empty !");
		} else if (isNullParams.isEmpty() && isEmptyParams.isEmpty()) {

			// If All the parameters are not null and not empty
			boolean isEmailExists = ifEmailExistsForUpdate(user.getIdUser(), user.getEmail());
			boolean isPhoneExists = ifPhoneExistsForUpdate(user.getIdUser(), user.getPhone());
			// Test if email and phone already exist
			if (!isEmailExists && !isPhoneExists) {

				User oldUser = findById(user.getIdUser());
				oldUser.setPhone(user.getPhone());
				oldUser.setEmail(user.getEmail());
				oldUser.setAboutMe(user.getAboutMe());
				
				
				if((user.getAvatar()!=null) && !((user.getAvatar().trim()).equals(""))) {
					oldUser.setAvatar(user.getAvatar());
				}
				Address oldAddress = addressDao.findById(oldUser.getAddress().getIdAddress());
				oldAddress.setWilaya(user.getAddress().getWilaya());
				oldAddress.setCommune(user.getAddress().getCommune());
				oldAddress.setLatitude(user.getAddress().getLatitude());
				oldAddress.setLongitude(user.getAddress().getLongitude());
				addressDao.edit(oldAddress);
				
				return edit(oldUser);

			} else if (isEmailExists && isPhoneExists) {
				throw new DataAlreadyExistsException("User Phone and Email already exit !");
			} else if (isEmailExists) {
				throw new DataAlreadyExistsException("User Email already exits !");
			} else if (isPhoneExists) {
				throw new DataAlreadyExistsException("User Phone already exits !");
			}
		}
		return false;
	}

	@Override
	public void editPassword(int userId, String oldPassword, String newPassword) {

		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(String.valueOf(userId));
		parameters.add(oldPassword);
		parameters.add(newPassword);

		// test if user exists or not
		if (findById(userId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id :" + userId + " not found  !");
		}

		String isNull = "";
		String isEmpty = "";

		if (oldPassword == null) {
			isNull = isNull + " oldPassword";
		}
		if (newPassword == null) {
			isNull = isNull + " newPassword";
		}
		if (oldPassword.equals("")) {
			isEmpty = isEmpty + " oldPassword";
		}
		if (newPassword.equals("")) {
			isEmpty = isEmpty + " newPassword";
		}

		if (!isNull.isEmpty() && !isEmpty.isEmpty()) {
			throw new EmptyParameterException(
					"Parameters : " + isNull + " Cannot be null !\nParameters : " + isEmpty + " Cannot be empty !");
		} else if (!isNull.isEmpty()) {
			throw new EmptyParameterException("Parameters : " + isNull + " Cannot be null !");
		} else if (!isEmpty.isEmpty()) {
			throw new EmptyParameterException("Parameters : " + isEmpty + " Cannot be empty !");
		} else if (isNull.isEmpty() && isEmpty.isEmpty()) {

			User user = findById(userId);
			if (isExpectedPassword(oldPassword, user.getSalt(), user.getPassword())) {

				try {
					byte[] salt = createSalte();
					String newPass = generatedHash(newPassword, salt);
					user.setSalt(salt);
					user.setPassword(newPass);

					session = HibernateUtil.getSession();
					session.beginTransaction();
					session.update(user);
					session.getTransaction().commit();
					session.close();
				} catch (Exception e) {
					throw new DatabaseException("DatabaseException User Password" + e.getMessage());
				}

			} else {
				throw new DataNotFoundException("Password is not correct !");
				// 400 //user password do not match
			}
		}

	}

	// this method test if there are empty parameters , for updating profile
	public String userHaveEmptyParameters(User user) {

		String isEmpty = "";
		if (user.getIdUser().toString().equals("")) {
			isEmpty = isEmpty + " IdUser";
		}
		if (user.getAddress().getWilaya().equals("")) {
			isEmpty = isEmpty + " Wilaya";
		}
		if (user.getAddress().getCommune().equals("")) {
			isEmpty = isEmpty + " Commune";
		}
		if (user.getPhone().equals("")) {
			isEmpty = isEmpty + " Phone";
		}
		if (user.getEmail().equals("")) {
			isEmpty = isEmpty + " Email";
		}

		return isEmpty;
	}

	// this method test if there are null parameters , for updating profile
	public String userHaveNullParameters(User user) {

		String isNull = "";
		if (user.getIdUser().toString() == null) {
			isNull = isNull + " IdUser";
		}
		if (user.getAddress().getWilaya() == null) {
			isNull = isNull + " Wilaya";
		}
		if (user.getAddress().getCommune() == null) {
			isNull = isNull + " Commune";
		}
		if (user.getPhone() == null) {
			isNull = isNull + " Phone";
		}
		if (user.getEmail() == null) {
			isNull = isNull + " Email";

		}

		return isNull;
	}

	@SuppressWarnings("unchecked")
	public boolean ifEmailExistsForUpdate(int idUser, String email) {
		boolean isExiste;
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			List<User> l = session.createQuery("from User as u where u.email ='" + email + "' and u.idUser !=" + idUser)
					.list();
			if (!l.isEmpty()) {
				isExiste = true;
			} else {
				isExiste = false;
			}
			session.getTransaction().commit();
			session.close();
			return isExiste;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException Address " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public boolean ifPhoneExistsForUpdate(int idUser, String phone) {
		boolean isExiste;
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();// or u.email ="+user+"
			List<User> l = session.createQuery("from User as u where u.phone ='" + phone + "'and u.idUser !=" + idUser)
					.list();
			if (!l.isEmpty()) {
				isExiste = true;
			} else {
				isExiste = false;
			}
			session.getTransaction().commit();
			session.close();
			return isExiste;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException Address " + e.getMessage());
		}
	}

	@Override
	public User findById(int i) {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			User c = (User) session.get(User.class, i);
			session.getTransaction().commit();
			session.close();
			if (c == null) {
				// throw exception User not found
				throw new DataNotFoundException("User with id : " + i + " not found  !");
			}
			return c;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public ArrayList<User> listAll() {

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			ArrayList<User> l = (ArrayList<User>) session.createQuery("from User").list();
			session.getTransaction().commit();
			session.close();
			return l;

		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean ifEmailExists(String email) {
		boolean isExiste;
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			List<User> l = session.createQuery("from User as u where u.email ='" + email + "'").list();
			if (!l.isEmpty()) {
				isExiste = true;
			} else {
				isExiste = false;
			}
			session.getTransaction().commit();
			session.close();

			return isExiste;

		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void sendGeneratedPasswordToEmail(String email) {
		User user = new User();
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			List<User> l = session.createQuery("from User as u where u.email ='" + email + "'").list();
			session.getTransaction().commit();
			session.close();
			if (!l.isEmpty()) {
				user = l.get(0);
			} else {
				throw new DataNotFoundException("This " + email + " dosen't existe");
			}

		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

		switch (user.getAccountActivation()) {

		case 0:
			throw new DataNotFoundException(
					"The account having this email " + email + " is not valiated yet, try again later!");

		case 1:
			// generate new password
			String newPassword = String.valueOf(geekPassword());
			try {
				byte[] salt = createSalte();
				String newPass = generatedHash(newPassword, salt);
				user.setSalt(salt);
				user.setPassword(newPass);
				session = HibernateUtil.getSession();
				session.beginTransaction();
				session.update(user);
				session.getTransaction().commit();
				session.close();

				// and send it to user email
				Util.sendEmail(email, "Password Reset", "you new password is : " + newPassword);
			} catch (Exception e) {
				throw new DatabaseException("DatabaseException User Password" + e.getMessage());
			}

			break;
		case 2:
			throw new DataNotFoundException("The account having this email " + email + " was deleted!");
		default :
			break;
		}

	}

	static char[] geekPassword() {
		String capitalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String smallChars = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String values = capitalChars + smallChars + numbers;
		Random rndmMethod = new Random();
		char[] password = new char[6];
		for (int i = 0; i < 6; i++) {
			password[i] = values.charAt(rndmMethod.nextInt(values.length()));
		}
		return password;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean ifPhoneExists(String phone) {

		boolean isExiste;

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();// or u.email ="+user+"
			List<User> l = session.createQuery("from User as u where u.phone ='" + phone + "'").list();
			if (!l.isEmpty()) {
				isExiste = true;
			} else {
				isExiste = false;
			}
			session.getTransaction().commit();
			session.close();
			return isExiste;
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public User login(String email, String password) {
		User u = null;

		// test if empty parameters
		if (email != null && password != null) {

			if (ifEmailExists(email)) {

				try {
					session = HibernateUtil.getSession();
					session.beginTransaction();// or u.email ="+user+"
					List<User> l = session.createQuery("from User as u where u.email ='" + email + "'").list();
					session.getTransaction().commit();
					session.close();

					if (!l.isEmpty()) {
						// 200 ok
						u = l.get(0);
						if (!isExpectedPassword(password, u.getSalt(), u.getPassword())) {
							// 400 //user password do not match
							throw new DataNotFoundException("User password do not match !");
						}

					}
				} catch (Exception e) {
					throw new DatabaseException(e.getMessage());
				}

			} else {
				// 401 //user not found (email don't exists)
				throw new DataNotFoundException("User not found (email " + email + " do not exists)!");
			}

		} else {
			// 422 //check which parameter is empty
			if (email == null && password == null) {
				throw new EmptyParameterException("Parameters Email and Password are empty !");
			} else if (email == null) {
				throw new EmptyParameterException("Parameter Email is empty !");

			} else if (password == null) {
				throw new EmptyParameterException("Parameter Password is empty !");
			}
		}

		return u;
	}


	// TODO here i didn't check for services if its available or not because if it
	// is not available it is not supposed to be in services list.
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<User> listAllByService(int serviceId, int clientId) {
		ServiceDao serviceDao = HibernateUtil.getServiceDao();
		// test if service exists or not
		if (serviceDao.findById(serviceId) == null) {
			// throw exception service not found
			throw new DataNotFoundException("Service with id : " + serviceId + " not found  !");
		}

		// test if user exists or not
		if (findById(clientId) == null) {
			// throw exception user not found
			throw new DataNotFoundException("User with id : " + clientId + " not found  !");
		}
		ArrayList<User> l = new ArrayList<>();
		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			l = (ArrayList<User>) session.createQuery("from User as u where u.idUser != " + clientId
					+ " and u.accountActivation != 2 and isActive = 1 and " + serviceId
					+ " in (Select idService from u.services)").list();
			session.getTransaction().commit();
			session.close();

		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
		return l;
	}

	private static String generatedHash(String data, byte[] salt) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.reset();
		digest.update(salt);

		byte[] hash = digest.digest(data.getBytes());
		return bytesToStringHex(hash);
	}

	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

	private static final  String bytesToStringHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v >>> 0x0F];
		}
		return new String(hexChars);
	}

	private static byte[] createSalte() {
		byte[] bytes = new byte[20];
		SecureRandom random = new SecureRandom();
		random.nextBytes(bytes);
		return bytes;
	}

	public static boolean isExpectedPassword(String password, byte[] salt, String expectedPassword) {

		String hasedPassword;
		try {
			hasedPassword = generatedHash(password, salt);
			if (hasedPassword.equals(expectedPassword)) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			throw new DatabaseException("A problem occured while testing the password");
		}

	}

	@Override
	public boolean setUserVisibilityInSearch(int userId, int visibility) {

		if (String.valueOf(userId) == null) {
			// throw exception Category null
			throw new DataNotFoundException("userId cannot be null! ");
		}

		// test if User dosn't exists
		if (findById(userId) == null) {
			// throw exception Category not found
			throw new DataNotFoundException("User with id :" + userId + " not found  !");
		}
		User user = findById(userId);
		user.setIsActive(((byte) visibility));

		try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			session.update(user);
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {
			throw new DatabaseException("DatabaseException User " + e.getMessage());
		}

	}
}
