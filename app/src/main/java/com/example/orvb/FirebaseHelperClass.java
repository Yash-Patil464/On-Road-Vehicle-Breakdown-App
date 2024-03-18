package com.example.orvb;

public class FirebaseHelperClass {
    String name, email, number, password;
    int user_type;

    public FirebaseHelperClass(String name, String email, String number, String password, int user_type) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password;
        this.user_type = user_type;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public FirebaseHelperClass() {
    }
}
