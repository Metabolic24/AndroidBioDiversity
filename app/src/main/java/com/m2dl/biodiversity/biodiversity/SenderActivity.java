package com.m2dl.biodiversity.biodiversity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.m2dl.biodiversity.biodiversity.sender.ISender;
import com.m2dl.biodiversity.biodiversity.sender.mail.MailSender;

public class SenderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sender_layout);
        Button btnSendTo = (Button) findViewById(R.id.btnSendTo);
        final UserInformation userInfo = (UserInformation) getIntent().getParcelableExtra("USER_INFORMATION");
        userInfo.loadImageFromDir(getCacheDir());
        ((ImageView) findViewById(R.id.imageView2)).setImageBitmap(userInfo.getImage());
        btnSendTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EditText txtSendTo = (EditText) findViewById(R.id.txtSendTo);
                        ISender mailSender = new MailSender(userInfo);
                        mailSender.sendData(txtSendTo.getText().toString());
                    }
                });
                t.start();
                SenderActivity.this.startActivity(new Intent(SenderActivity.this, MainActivity.class));
            }
        });

        MetaXMLWriter.saveXml(this.getCacheDir(), userInfo);
    }
}
