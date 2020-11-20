package es.covid_free.MAIL;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import java.net.URL;

public class MailWrapper {

	public static void sendEmail(String direccion) {
		try {
			// Create the email message
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("username", "password"));
			email.setSSLOnConnect(true);
			
			email.addTo("jdoe@somewhere.org", "John Doe");
			email.setFrom("me@apache.org", "Me");
			email.setSubject("Test email with inline image");
			  
			// embed the image and get the content id
			URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
			String cid = email.embed(url, "Apache logo");
			  
			// set the html message
			email.setHtmlMsg("<html>The apache logo - <img src=\"cid:"+cid+"\"></html>");
	
			// set the alternative message
			email.setTextMsg("Your email client does not support HTML messages");
	
			// send the email
			email.send();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
