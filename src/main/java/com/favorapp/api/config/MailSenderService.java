package com.favorapp.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.favorapp.api.user.User;

@Service
public class MailSenderService {

	private JavaMailSender javaMailSender;
	
	@Autowired
	public MailSenderService(JavaMailSender javaMailSender){
		this.javaMailSender = javaMailSender;
	}
	
	public void sendEmail(User user) throws MailException, InterruptedException {

        System.out.println("Sending email...");
        
        SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		//mail.setFrom("demomail@gmail.com");
		mail.setSubject("Spring Boot is awesome!");
		mail.setText("Why aren't you using Spring Boot?");
		javaMailSender.send(mail);
		
		System.out.println("Email Sent!");
	}
	
	public void sendEmailWithDetails(String emailaddress, String subject, String text) throws MailException, InterruptedException {

        System.out.println("Sending email...");
        
        SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(emailaddress);
		//mail.setFrom("demomail@gmail.com");
		mail.setSubject(subject);
		mail.setText(text);
		javaMailSender.send(mail);
		
		System.out.println("Email Sent!");
	}
	
}
