package es.covid_free.MAIL;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailWrapper {
	private static Session getSession() {
	    final String username	= "no.reply.CovidFree@gmail.com";
	    final String password	= "CDrTk7mm8DgxmKa";
	    final String host		= "smtp.gmail.com";
	    
	    Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true"); //TLS
	    
	    // Get the Session object.
        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        return session;
	}
	
	/**
	 * Dada una dirección de correo electrónico, se le notifica que ha
	 * estado en contacto con un positivo de COVID-19
	 * 
	 * @param direccion Direccion de correo a la que notificar
	 */
	public static void sendEmail(String direccion) {
		// Sender's email ID needs to be mentioned
	    String from = "no.reply.CovidFree@gmail.com";
		Session session = getSession();

		try {	
			// Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse(direccion));

            // Set Subject: header field
            message.setSubject("Contacto con un positivo");

            // Send the actual HTML message, as big as you like
            message.setContent(
            "<h1>Contacto con un positivo</h1><div>Ha estado en contacto con un positivo de COVID-19 en los"+
            " útlimos 3 días. Por favor, <strong>pongase en contacto con las autoridades competentes</strong>.</div>",
             "text/html");

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dada una lista de direcciones de correo electrónico, se les
	 * notifica que han estado en contacto con un positivo de COVID-19
	 * siendo el mismo correo para todos pero en copia de carbono oculta
	 * 
	 * @param direccion Direcciones de correo a la que notificar
	 */
	public static void sendEmail(String direccion[]) {
		// Sender's email ID needs to be mentioned
	    String from = "no.reply.CovidFree@gmail.com";
		String dirs = String.join(",", direccion);
		Session session = getSession();

		try {	
			// Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.BCC,
              InternetAddress.parse(dirs));

            // Set Subject: header field
            message.setSubject("Contacto con un positivo");

            // Send the actual HTML message, as big as you like
            message.setContent(
              "<h1>Contacto con un positivo</h1><div>Ha estado en contacto con un positivo de COVID-19 en los"+
              " útlimos 3 días. Por favor, <strong>pongase en contacto con las autoridades competentes</strong>.</div>",
             "text/html");

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String Args[]) {
		String[] correos = new String[] {"a@a","774156@unizar.es"};
		sendEmail(correos);
	}
}
