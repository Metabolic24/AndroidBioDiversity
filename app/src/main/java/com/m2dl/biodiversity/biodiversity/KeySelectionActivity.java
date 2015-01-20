package com.m2dl.biodiversity.biodiversity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class KeySelectionActivity extends Activity {

    private UserInformation userInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_selection);

        //userInfo = (UserInformation) getIntent().getSerializableExtra("USER_INFORMATION");

        KeyXMLParser parser = new KeyXMLParser();

        try {
            final List<BioType> vals = parser.parse(this.getResources().openRawResource(R.raw.key));

            List<String> spinnerArray = new ArrayList<String>();
            for (BioType bt : vals) {
                spinnerArray.add(bt.id);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner sItems = (Spinner) findViewById(R.id.spinner);
            sItems.setAdapter(adapter);

            sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Spinner spinner = (Spinner) findViewById(R.id.spinner);
                    String text = spinner.getSelectedItem().toString();
                    for (BioType bt : vals) {
                        if (bt.id.equals(text)) {
                            initSpinner2(bt.soustypes);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (XmlPullParserException ex) {

        } catch (IOException x) {
        }

        Button nextButton = (Button) findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner sItems = (Spinner) findViewById(R.id.spinner2);
                userInfo.setKey(sItems.getSelectedItem().toString());
                stop(RESULT_OK);
            }
        });

        Button passButton = (Button) findViewById(R.id.button_pass);
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(RESULT_OK);
            }
        });

        Button cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(RESULT_CANCELED);
            }
        });

    }

    public void stop(int resultCode) {
        this.setVisible(false);
        setResult(resultCode);
        finish();
    }


    public void initSpinner2(List<String> soustypes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, soustypes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner2);
        sItems.setAdapter(adapter);
    }
}
