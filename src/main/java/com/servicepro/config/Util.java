package com.servicepro.config;

import java.io.StringWriter;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Util {

	
	public static String objectToJson(Object object) {
		
		 StringWriter stringEmp = null;
	      try {

            ObjectMapper objectMapper = new ObjectMapper();
            stringEmp = new StringWriter();
            objectMapper.writeValue(stringEmp, object);
            } catch (Exception e) {
            	//catch exception
            }

      return stringEmp.toString();
	}
	
	public static void sendEmail(String sendto, String sbjct , String msgText ) {
		  String host="smtp.gmail.com";  
		  final String user="";//change accordingly  
		  final String password="";//change accordingly  
		    
		  String to=sendto;//change accordingly  
		  
		   //Get the session object  
		   Properties props = new Properties();  
           props.put("mail.smtp.starttls.enable", "true");
           props.put("mail.smtp.host", host);
           props.put("mail.smtp.port", "25");
           props.put("mail.smtp.auth", "true");
           props.put("mail.smtp.starttls.required", "true");
           props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
           		
		     
		   Session session = Session.getDefaultInstance(props,  
		    new javax.mail.Authenticator() {  
			   @Override
		      protected PasswordAuthentication getPasswordAuthentication() {  
		    return new PasswordAuthentication(user,password);  
		      }  
		    });  
		  
		   //Compose the message  
		    try {  
		     MimeMessage message = new MimeMessage(session);  
		     message.setFrom(new InternetAddress(user));  
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
		     message.setSubject(sbjct);  
		     message.setText(msgText);  
		       
		    //send the message  
		     Transport.send(message);  
		   
		     } catch (MessagingException e) {
		    	// catch exception
		     }  
		 
		
	}

}
