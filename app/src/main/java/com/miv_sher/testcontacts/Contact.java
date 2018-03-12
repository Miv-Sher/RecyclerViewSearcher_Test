package com.miv_sher.testcontacts;

import android.net.Uri;

/**
 * Created by Nessa on 12.03.2018.
 */

public class Contact {
    private String name = "";
    private String phone = "";
    private int id;
    private Uri photo;

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }
}
