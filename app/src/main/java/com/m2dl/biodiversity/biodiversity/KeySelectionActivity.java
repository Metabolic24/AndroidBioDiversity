package com.m2dl.biodiversity.biodiversity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class KeySelectionActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_selection);

        KeyXMLParser parser = new KeyXMLParser();

        try {
            final List<BioType> vals = parser.parse(this.getResources().openRawResource(R.raw.key));

            List<String> spinnerArray =  new ArrayList<String>();
            for(BioType bt : vals){
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
                    Spinner spinner = (Spinner)findViewById(R.id.spinner);
                    String text = spinner.getSelectedItem().toString();
                    for(BioType bt : vals){
                        if(bt.id.equals(text)){
                            initSpinner2(bt.soustypes);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        catch (XmlPullParserException x){
        }
        catch (IOException x2){
        }

    }

    public void initSpinner2(List<String> soustypes){
        /*List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("test");
        spinnerArray.add("blabla");*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, soustypes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner2);
        sItems.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_key_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
