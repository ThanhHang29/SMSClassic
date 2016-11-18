package com.ttth.model;

/**
 * Created by Thanh Hang on 4/22/2016.
 */
public class SMS {
    private int id;
    private String name;
    private String purport;
    private String date;
    private String time;
    private String phoneNumber;

    public SMS(int id, String name, String purport, String date, String time, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.purport = purport;
        this.date = date;
        this.time = time;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                "\nname=" + name +
                "\npurport=" + purport +
                "\ndate=" + date +
                "\ntime=" + time+
                        "\nphone="+phoneNumber+"\n" ;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPurport() {
        return purport;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
