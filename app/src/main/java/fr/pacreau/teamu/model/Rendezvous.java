package fr.pacreau.teamu.model;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Created by spacreau on 01/06/2017.
 */

public class Rendezvous implements Parcelable {

    private String uid;
    private String name;
    private Date date;
    private String address;
    private Player washer;

    public enum RendezVousType {
        TOURNOI,
        PLATEAU
    }
    private RendezVousType type;

    private List<Player> invitedPlayerList;
    private List<Player> offPlayerList;
    private List<Player> injuredPlayerList;


    public Rendezvous() {

    }

    public Rendezvous(Date p_oDate, String p_oAddress) {
        this.date = p_oDate;
        this.address = p_oAddress;
    }

    public Rendezvous(Parcel parcel) {
        this.uid = parcel.readString();
        this.name = parcel.readString();
        this.date = new Date(parcel.readLong());
        this.address = parcel.readString();
        this.washer = parcel.readParcelable(Rendezvous.class.getClassLoader());
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
        parcel.writeLong(date.getTime());
        parcel.writeString(address);
        parcel.writeParcelable(washer,0);
    }

    public static final Parcelable.Creator<Rendezvous> CREATOR = new Parcelable.Creator<Rendezvous>() {
        public Rendezvous createFromParcel(Parcel in) {
            return new Rendezvous(in);
        }

        public Rendezvous[] newArray(int size) {
            return new Rendezvous[size];
        }
    };

    @Override
    public String toString() {
        return "uid" + this.uid + "name" + this.name + "date" + this.date
                + "washer " + this.washer + "address " + this.address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RendezVousType getType() {
        return type;
    }

    public void setType(RendezVousType type) {
        this.type = type;
    }

    public List<Player> getInvitedPlayerList() {
        return invitedPlayerList;
    }

    public void setInvitedPlayerList(List<Player> invitedPlayerList) {
        this.invitedPlayerList = invitedPlayerList;
    }

    public List<Player> getOffPlayerList() {
        return offPlayerList;
    }

    public void setOffPlayerList(List<Player> offPlayerList) {
        this.offPlayerList = offPlayerList;
    }

    public List<Player> getInjuredPlayerList() {
        return injuredPlayerList;
    }

    public void setInjuredPlayerList(List<Player> injuredPlayerList) {
        this.injuredPlayerList = injuredPlayerList;
    }

    public Player getWasher() {
        return washer;
    }

    public void setWasher(Player washer) {
        this.washer = washer;
    }
}
