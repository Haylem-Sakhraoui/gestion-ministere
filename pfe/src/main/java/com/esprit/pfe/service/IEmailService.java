package com.esprit.pfe.service;

import com.esprit.pfe.DTO.Mail;
import jakarta.mail.MessagingException;

public interface  IEmailService {
    void sendMail(Mail mail) throws MessagingException;
}
