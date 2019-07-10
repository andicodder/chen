package com.lilliemountain.chen.pojo;


public class Students {

    private String uID;
    private String email;
    private String name;
    private String storage;
    private String grade;
    private String _class;
    private String address;
    private String contact;
    private String guardianemail;
    private String password;
    private String rollno;
    public Students() {
    }

    public Students(String uID, String email, String name, String storage, String grade, String _class, String address, String contact, String guardianemail, String password, String rollno) {
        this.uID = uID;
        this.email = email;
        this.name = name;
        this.storage = storage;
        this.grade = grade;
        this._class = _class;
        this.address = address;
        this.contact = contact;
        this.guardianemail = guardianemail;
        this.password = password;
        this.rollno = rollno;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getUID() {
        return uID;
    }

    public void setUID(String uID) {
        this.uID = uID;
    }
    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGuardianemail() {
        return guardianemail;
    }

    public void setGuardianemail(String guardianemail) {
        this.guardianemail = guardianemail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
