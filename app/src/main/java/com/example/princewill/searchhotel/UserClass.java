package com.example.princewill.searchhotel;

/**
 * Created by Princewill on 1/15/2017.
 */

public class UserClass {
    private String username;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String country;
    private String state;

    public UserClass(String user[])
    {

        setUsername(user[0]);
        setEmail(user[1]);
        setPassword(user[2]);
        setFirstname(user[3]);
        setLastname(user[4]);
        setCountry(user[5]);
        setState(user[6]);

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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
