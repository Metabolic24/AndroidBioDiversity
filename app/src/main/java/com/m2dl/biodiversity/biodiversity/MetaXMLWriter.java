package com.m2dl.biodiversity.biodiversity;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Hugo on 18/01/2015.
 */
public class MetaXMLWriter {

    public static void saveXml(File outputDir, UserInformation userInfo){
        try {
            File outputFile = File.createTempFile("meta", "xml", outputDir);

            FileOutputStream fileos = new FileOutputStream(outputFile);

            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "photo");

            serializer.startTag("", "coord");
            serializer.startTag("", "lati");
            if(userInfo.getLocation() != null){
                serializer.text(String.valueOf(userInfo.getLocation().getLatitude()));
            }
            serializer.endTag("", "lati");
            serializer.startTag("", "longi");
            if(userInfo.getLocation() != null){
                serializer.text(String.valueOf(userInfo.getLocation().getLongitude()));
            }
            serializer.endTag("", "longi");
            serializer.endTag("", "coord");
            serializer.startTag("", "date");
            serializer.text(userInfo.getDate());
            serializer.endTag("", "date");
            serializer.startTag("", "user");
            serializer.text(userInfo.getLogin());
            serializer.endTag("", "user");
            serializer.startTag("", "com");
            serializer.text(userInfo.getComment());
            serializer.endTag("", "com");
            serializer.startTag("", "point");
            serializer.startTag("", "cle");
            serializer.text(userInfo.getKey());
            serializer.endTag("", "cle");
            serializer.startTag("", "zone");
            serializer.text("coords zone...");
            serializer.endTag("", "zone");
            serializer.endTag("", "point");

            serializer.endTag("", "photo");
            serializer.endDocument();



            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();

            Log.i("MetaXMLWriter", "Finish");
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
