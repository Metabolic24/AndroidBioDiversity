package com.m2dl.biodiversity.biodiversity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by loic on 15/01/15.
 */
public class SenderActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ISender mailSender = new MailSender();
                mailSender.sendData("jhon@gmail.com", "loicfaure@hotmail.fr");
            }
        });
        t.start();

    }
}
