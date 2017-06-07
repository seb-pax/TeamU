package fr.pacreau.teamu.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;

/**
 * Created by spacreau on 01/06/2017.
 */

public class Player  implements Parcelable {

    public String uid = "";
    public String name = "";
    public String email = "";
    public String fullname = "";

    public Player() {

    }

    public Player(Parcel parcel) {
        uid = parcel.readString();
        name = parcel.readString();
        email = parcel.readString();
        fullname = parcel.readString();
    }

    public Player(String p_name, String p_fullname, String p_email) {
        name = p_name;
        fullname = p_fullname;
        email = p_email;
    }


    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(fullname);
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public String toString() {
        return "uid" + this.uid + "name" + this.name + "email" + this.email
                + "fullname" + this.fullname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String puid) {
        this.uid = puid;
    }

    public String getName() {
        return name;
    }

    public void setName(String pname) {
        this.name = pname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String pemail) {
        this.email = pemail;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String pfullname) {
        this.fullname = pfullname;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            return this.getUid().equals(((Player)obj).getUid());
        }
        return false;
    }
}
