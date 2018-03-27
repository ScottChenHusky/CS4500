package edu.northeastern.cs4500.services;// Created by xuanyuli on 3/6/18.

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class CustomerPhoneNumber {
  // Find your Account Sid and Token at twilio.com/user/account
  public static final String ACCOUNT_SID = "AC916d339685776d381e560adfb97e20dc";
  public static final String AUTH_TOKEN = "83a41d12fb0962ae0943e16de5c36724";

  public static void sendCodeToPhone(String number) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);


    Message message = Message.creator(new PhoneNumber(number),

            new PhoneNumber("+19782881503"),
            "Welcome to Spoiled Tomatillos! This is team-34!").create();

    System.out.println(message.getSid());
  }

}