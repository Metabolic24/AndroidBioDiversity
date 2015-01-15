package com.m2dl.biodiversity.biodiversity;

public class MailSender implements ISender {

    @Override
    public void sendData(String mailSender, String mailRecipient) {
        GMailSender sender = new GMailSender("biodiversityapp@gmail.com", "bioappbio");
        //sender.addAttachment("filename", "subject");
        //sender.addAttachment("filename", "subject");

        sender.sendMail("Subject", "This is Body", mailSender, mailRecipient);

    }
}
