package es.covid_free.MAIL;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailWrapper {

	public static void sendEmail(String direccion) {
			// Sender's email ID needs to be mentioned
		    String from = "no.reply.CovidFree@gmail.com";
		    final String username	= "no.reply.CovidFree@gmail.com";
		    final String password	= "CDrTk7mm8DgxmKa";
		    final String host		= "smtp.gmail.com";
		    
		    Properties prop = new Properties();
	        prop.put("mail.smtp.host", "smtp.gmail.com");
	        prop.put("mail.smtp.port", "465");
	        prop.put("mail.smtp.auth", "true");
	        prop.put("mail.smtp.ssl.enable", "true"); //TLS
		    
		    // Get the Session object.
	        Session session = Session.getInstance(prop,
	                new javax.mail.Authenticator() {
	                    protected PasswordAuthentication getPasswordAuthentication() {
	                        return new PasswordAuthentication(username, password);
	                    }
	                });

		try {	
			// Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse(direccion));

            // Set Subject: header field
            message.setSubject("Testing Subject");

            // Send the actual HTML message, as big as you like
            message.setContent(
              "<h1>This is actual message embedded in HTML tags</h1>",
             "text/html");

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String Args[]) {
		sendEmail("774156@unizar.es");
	}
}
