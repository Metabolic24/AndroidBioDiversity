package com.m2dl.biodiversity.biodiversity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

/**
 * Created by loic on 15/01/15.
 */
public class MailSender implements ISender {

    @Override
    public void sendData(String mailSender, String mailRecipient) {
        GMailSender sender = new GMailSender("biodiversityapp@gmail.com", "bioappbio");
        //sender.addAttachment("filename", "subject");
        //sender.addAttachment("filename", "subject");

        sender.sendMail("Subject", "This is Body", mailSender, mailRecipient);

    }
}
