package com.m2dl.biodiversity.biodiversity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by loic on 18/01/15.
 */
public class UserInformation implements Parcelable{

    public static final Parcelable.Creator<UserInformation> CREATOR = new Parcelable.Creator<UserInformation>() {

        @Override
        public UserInformation createFromParcel(Parcel source) {
            return new UserInformation(source);
        }

        @Override
        public UserInformation[] newArray(int size) {
            return new UserInformation[size];
        }
    };

    private String comment = "";
    private String login = "";
    private String date = "";
    private String key = "";
    private Location location = null;
    private Bitmap image;
    private File keyCharFile;
    private String fileName;


    public UserInformation() {
        this("", null, "");
    }

    public UserInformation(Parcel parcel) {
        login = parcel.readString();
        comment = parcel.readString();
        date = parcel.readString();
        key = parcel.readString();
        location = parcel.readParcelable(Location.class.getClassLoader());

    }

    public UserInformation(String login, Location location, String comment) {
        this.login = login;
        this.location = location;
        this.comment = comment;
        image = null;
        keyCharFile = null;
        fileName = "";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public File getKeyCharFile() {
        return keyCharFile;
    }

    public void setKeyCharFile(File keyCharFile) {
        this.keyCharFile = keyCharFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void saveImageToDir(File outputDir) {
        try {
            File outputFile = File.createTempFile("meta", "png", outputDir);

            FileOutputStream fileOS = new FileOutputStream(outputFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            fileOS.write(stream.toByteArray());
            fileOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadImageFromDir(File inputDir) {
        File file = new File(inputDir, "/meta.png");
        fileName = file.getAbsolutePath();
        image = BitmapFactory.decodeFile(fileName);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(comment);
        dest.writeString(date);
        dest.writeString(key);
        dest.writeParcelable(location, flags);
    }
}

