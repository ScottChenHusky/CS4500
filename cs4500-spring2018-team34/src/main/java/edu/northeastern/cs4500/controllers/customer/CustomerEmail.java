package edu.northeastern.cs4500.controllers.customer;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CustomerEmail {
  public static void main(String[] args) {
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class",
            "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");

    Session session = Session.getDefaultInstance(props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("spoiledtomatillos@gmail.com",
                        "SpoiledTomatillosSpoiledTomatillos");
              }
            });

    try {

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("spoiledtomatillos@gmail.com"));
      message.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse("bergmann.t@husky.neu.edu"));
      message.setSubject("Spoiled Tomatillos");
      message.setText("Dear Travis Bergmann:" +
              "\n\n This is Spoiled Tomatillos! Thanks for using our products! :)" +
              "\n\n - Team 34");

      Transport.send(message);

      System.out.println("Done");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}