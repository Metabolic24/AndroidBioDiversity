package com.m2dl.biodiversity.biodiversity.sender.mail;

import com.m2dl.biodiversity.biodiversity.UserInformation;
import com.m2dl.biodiversity.biodiversity.sender.ISender;

import javax.mail.MessagingException;

public class MailSender implements ISender {

    private UserInformation infos;

    public MailSender(UserInformation infos) {
        this.infos = infos;
    }


    @Override
    public void sendData(String mailRecipient) {
        GMailSender sender = new GMailSender("biodiversityapp@gmail.com", "bioappbio");
        try {
            sender.addAttachment(infos.getFileName(), "subject");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        //sender.addAttachment("filename", "subject");

        sender.sendMail("Subject", "This is Body", "biodiversityapp@gmail.com", mailRecipient);

    }
}
