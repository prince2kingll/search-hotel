package com.example.princewill.searchhotel;

/**
 * Created by Princewill on 1/15/2017.
 */

public class UserClass {
    private static String username;
    private String email;
    private String password;
    private String firstname;
    private String lastname;

    public UserClass(String user[])
    {
        setUsername(user[0]);
        setEmail(user[2]);
        setPassword(user[1]);
        setFirstname(user[3]);
        setLastname(user[4]);
    }



    public static String getUsername() {
        return username;
    }

    public static void setUsername(String usernam) {
        username = usernam;
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
}
