package com.example.microfinancepi.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import com.twilio.converter.Promoter;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
@ConfigurationProperties(prefix = "twilio")
public class TwilioService {

    @Value("${TWILIO.ACCOUNT_SID}")
    String ACCOUNT_SID;
    @Value("${TWILIO.AUTH_TOKEN}")
    String AUTH_TOKEN;
    @Value("${TWILIO.PHONE_NUMBER}")
    String PHONE_NUMBER;


    @PostConstruct
    private void setup(){
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
    }
    public String sendSMS(String smsNumb, String smsMesg){
        Message msg= Message.creator(
                new PhoneNumber(smsNumb),
                new PhoneNumber(PHONE_NUMBER),
                smsMesg).create();
        return msg.getStatus().toString();
    }
}

