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

    final String username = "spoiledtomatillos@gmail.com";
    final String password = "SpoiledTomatillosSpoiledTomatillos";

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "stmp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
              }
            });

    try {

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("spoiledtomatillos@gmail.com"));
      message.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse("lixuanyu1993@gmail.com"));
      message.setSubject("Testing Subject");
      message.setText("Dear Spoiled Tomatillos User,"
              + "\n\n No spam to my email, please!");

      Transport.send(message);

      System.out.println("Done");

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
