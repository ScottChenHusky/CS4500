package edu.northeastern.cs4500.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class CustomerEmail {
  public static void sendEmail(String recipient, String subject, String text) {
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
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
      message.setSubject(subject);
      message.setText(text);

      Transport.send(message);

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
  public static void sendRegistrationEmail(String recipient, String name) {
    if (recipient == null || recipient.equals("") || name == null || name.equals("")) {
      throw new IllegalArgumentException("empty");
    }
    new Thread(() -> {
      String subject = "Spoiled Tomatillos";
      String text = "Dear " + name + ":" +
              "\n\n This is Spoiled Tomatillos! Thanks for using our products! :)" +
              "\n\n - Team 34";
      sendEmail(recipient, subject, text);
    }).start();
  }
  public static void sendAdminCodeEmail(String recipient, String name, String adminCode) {
    if (recipient == null || recipient.equals("") || name == null || name.equals("") ||
            adminCode == null || adminCode.equals("")) {
      throw new IllegalArgumentException("empty");
    }
    new Thread(() -> {
      String subject = "Spoiled Tomatillos";
      String text = "Dear " + name + ":" +
              "\n\n This is Spoiled Tomatillos! Thank you for your interest in joining us!" +
              "\n\n Your admin code is " + adminCode +
              "\n\n Please use this code immediately. It will be expired after 10 minutes." +
              "\n\n - Team 34";
      sendEmail(recipient, subject, text);
    }).start();
  }
}