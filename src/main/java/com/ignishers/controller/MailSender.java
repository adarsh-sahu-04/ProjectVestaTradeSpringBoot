package com.ignishers.controller;

import java.util.Properties;

import org.springframework.stereotype.Component;

import com.ignishers.pojo.User;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailSender {

	final static String fromEmail="adarshsahu231011@gmail.com";
	final static String pass="agyoaclrqltuhgtd";
	
//--------------------------------------------------------------------------------------------------------------------------
	public static void sendMailForRegister(User a) {
		/* send email for registration and shows pending
		 * 
		 * 
		 * 
		 */
		
		
		Properties props=new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		
		
		
		Session session=Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, pass);
			}
		});
		try {
			Message message=new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(a.getEmail()));
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("ajaysinghkhichi5@gmail.com"));
			
			
			message.setSubject("Registration Initiated on VestraTrade");
			String msg = "<p>Dear <strong>" + a.getFirstName()+",<br>"
					+ "Your Account is Created Successfully</strong>, and Pending for Approval.</p>"
					+ "<p>Wait for 3 to 4 Days.</p>"
					+ "<br><br><h2>Thank You for Register with Us.</h2><br><br><br>"
					+ "<h1 style=\"color: #000; font-weight: bold;\">Team Vesta<span style=\"color: #ff5733; font-weight: normal;\">Trade</span></h1>";
			
			message.setContent(msg, "text/html");
			Transport.send(message);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
//--------------------------------------------------------------------------------------------------------------------------
public static void sendMailAfterVerify(User a) {
	/* send email after account is verified by admin
	 * 
	 * all details are correct.
	 * 
	 * 
	 * 
	 */
		
		
		Properties props=new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		
		
		
		Session session=Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, pass);
			}
		});
		try {
			Message message=new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(a.getEmail()));
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("ajaysinghkhichi5@gmail.com"));
			
			
			message.setSubject("Account Verified on VestraTrade");
			String msg = "<p>Dear <strong>" + a.getFirstName()+",<br>"
					+ "<p><strong>Your Account is Verified, Now You can Login and Start Trading.</strong></p>"
					+ "<p>Your CKYC Request is Done</p>"+
					"<p>User Credencials for Login: </p>"
					+ "<p>Email: " + a.getEmail() + "</p>"
					+ "<p>Password: " + a.getPassword()+"</p>"
					+ "<br><br><h2>Thank You for Register with Us.</h2><br><br><br>"
					+ "<h1 style=\"color: #000; font-weight: bold;\">Team Vesta<span style=\"color: #ff5733; font-weight: normal;\">Trade</span></h1>";
			
			message.setContent(msg, "text/html");
			Transport.send(message);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


//--------------------------------------------------------------------------------------------------------------------------
	public static void sendMailAfterRejection(User a) {
	/* send email after account is rejected by admin
	 * 
	 * Details mismatch.
	 */
		
		
		Properties props=new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		
		
		
		Session session=Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, pass);
			}
		});
		try {
			Message message=new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(a.getEmail()));
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("ajaysinghkhichi5@gmail.com"));
			
			
			message.setSubject("Account Rejected by VestraTrade");
			String msg = "<p>Dear <strong>" + a.getFirstName()+",<br>"
					+ "<p>We Regrate to Inform You That Your<strong> Account is Rejected</strong>, because Some Information that You Provided are Appear to be Wrong.</p>"
					+ "<p>Your CKYC Request is also Rejected.</p>"
					+ "<br><br><h2>Contact Us for more Info, or Create New Account with Proper Details.</h2><br><br><br>"
					+ "<h1 style=\"color: #000080; font-weight: bold;\">Team Vesta<span style=\"color: #000; font-weight: normal;\">Trade</span></h1>";
			
			message.setContent(msg, "text/html");
			Transport.send(message);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

//--------------------------------------------------------------------------------------------------------------------------
	public static void sendMailAfterSuspention(User a) {
		/* send email after account is blocked or suspended by Admin
		 * 
		 * Suspicious Activity
		 */
			
			
			Properties props=new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			
			
			
			Session session=Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, pass);
				}
			});
			try {
				Message message=new MimeMessage(session);
				message.setFrom(new InternetAddress(fromEmail));
				
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(a.getEmail()));
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("ajaysinghkhichi5@gmail.com"));
				
				
				message.setSubject("Alert! Suspicious Acitvity Detected");
				String msg = "<p>Dear <strong>" + a.getFirstName()+",<br>"
						+ "<p>This Mail is to Inform You That Your<strong> Account is Suspended</strong>, because of Some Suspicious Activity Done By Your Account.</p>"
						+ "<br><br><h2>Contact Us for more Information</h2><br><br><br>"
						+ "<h1 style=\"color: #000080; font-weight: bold;\">Team Vesta<span style=\"color: #000; font-weight: normal;\">Trade</span></h1>";
				
				message.setContent(msg, "text/html");
				Transport.send(message);
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
}
