package com.m2dl.biodiversity.biodiversity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.m2dl.biodiversity.biodiversity.sender.ISender;
import com.m2dl.biodiversity.biodiversity.sender.mail.MailSender;

public class SenderActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sender_layout);
        Button btnSendTo = (Button) findViewById(R.id.btnSendTo);
        btnSendTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EditText txtSendTo = (EditText) findViewById(R.id.txtSendTo);
                        ISender mailSender = new MailSender();
                        mailSender.sendData(txtSendTo.getText().toString());
                    }
                });
                t.start();
                SenderActivity.this.startActivity(new Intent(SenderActivity.this, MainActivity.class));
            }
        });

        UserInformation userInfo = (UserInformation) getIntent().getParcelableExtra("USER_INFORMATION");
        MetaXMLWriter.saveXml(this.getCacheDir(), userInfo);
    }
}
