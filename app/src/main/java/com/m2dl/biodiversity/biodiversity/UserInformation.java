package com.m2dl.biodiversity.biodiversity;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by loic on 18/01/15.
 */
public class UserInformation implements Parcelable{
    private String comment = "";
    private String login = "";
    private Location location = null;
    private Bitmap image;
    private File keyCharFile;


    public UserInformation() {
        this("", null, "");
    }

    public UserInformation(String login, Location location, String comment) {
        this.login = login;
        this.location = location;
        this.comment = comment;
        image = null;
        keyCharFile = null;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(comment);
        dest.writeParcelable(image, flags);
        dest.writeParcelable(location, flags);

        /*private String comment = "";
        private String login = "";
        private Location location = null;
        private Bitmap image;
        private File keyCharFile;*/

    }
}
