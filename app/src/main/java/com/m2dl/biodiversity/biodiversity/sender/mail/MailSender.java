package com.m2dl.biodiversity.biodiversity.sender.mail;

import com.m2dl.biodiversity.biodiversity.sender.ISender;

public class MailSender implements ISender {

    @Override
    public void sendData(String mailRecipient) {
        GMailSender sender = new GMailSender("biodiversityapp@gmail.com", "bioappbio");
        //sender.addAttachment("filename", "subject");
        //sender.addAttachment("filename", "subject");

        sender.sendMail("Subject", "This is Body", "biodiversityapp@gmail.com", mailRecipient);

    }
}
