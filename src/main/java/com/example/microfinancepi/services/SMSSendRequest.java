package com.example.microfinancepi.services;

import lombok.Data;

@Data
public class SMSSendRequest {
    private String destSMS;
    private String smsMessage;

}
