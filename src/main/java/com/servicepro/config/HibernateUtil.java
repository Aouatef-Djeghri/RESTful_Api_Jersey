package com.servicepro.config;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import com.servicepro.dao.impl.*;
import com.servicepro.dao.interfaces.*;
import com.servicepro.entities.*;





public class HibernateUtil {

	//Create sessionFactory
    public static final SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    static {
    	try {
    		
    		Configuration configuration = new Configuration().configure()//"hibernate.cfg.xml"
    	            .addAnnotatedClass(Review.class)
    	            .addAnnotatedClass(Work.class)
    	            .addAnnotatedClass(Service.class)       
    	            .addAnnotatedClass(Address.class)  
    	            .addAnnotatedClass(User.class)  
    	            .addAnnotatedClass(Categorie.class)
    	            .addAnnotatedClass(Application.class);
    	    serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
    	            configuration.getProperties()).build();
    	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    	    
	    	}catch(Exception ex)
    	{
			throw new ExceptionInInitializerError(ex);
    	}
    }
    public static SessionFactory getSessionFactory() {
    	return sessionFactory;
    }
    public static Session getSession() throws HibernateException {
        return sessionFactory.openSession();
    }
    
    
    public static ReviewDao getReviewDao()
    {
    	return new ReviewDaoImpl();
    }
    
    public static WorkDao getWorkDao()
    {
    	return new WorkDaoImpl();
    }
    
    public static ServiceDao getServiceDao()
    {
    	return new ServiceDaoImpl();
    }
    
    public static CategorieDao getCategorieDao()
    {
    	return new CategorieDaoImpl();
    }
  
    public static AddressDao getAddressDao()
    {
    	return new AddressDaoImpl();
    }
    
    
    public static UserDao getUserDao()
    {
    	return new UserDaoImpl();
    }
    
    public static ApplicationDao getApplicationDao()
    {
    	return new ApplicationDaoImpl();
    }
}
