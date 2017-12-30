package com.appsinventiv.classifiedads.Model;

/**
 * Created by AliAh on 29/12/2017.
 */

public class User {
    String name, username, email, password, phone, city, active, code,codeSent;

    public User() {
    }

    public User(String name, String username, String email, String password, String phone, String city, String active, String code, String codeSent) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.city = city;
        this.active = active;
        this.code = code;
        this.codeSent = codeSent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeSent() {
        return codeSent;
    }

    public void setCodeSent(String codeSent) {
        this.codeSent = codeSent;
    }
}