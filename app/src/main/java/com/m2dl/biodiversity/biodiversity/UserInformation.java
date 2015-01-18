package com.m2dl.biodiversity.biodiversity;

import android.location.Location;

/**
 * Created by loic on 18/01/15.
 */
public class UserInformation {
    private String comment = "";
    private String login = "";
    private Location location = null;

    public UserInformation() {
        this("", null, "");
    }

    public UserInformation(String login, Location location, String comment) {
        this.login = login;
        this.location = location;
        this.comment = comment;
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
}
