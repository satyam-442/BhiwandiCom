package com.example.bhiwandicom.Model;

public class User {
    public String Name, Password, Phone, ShopName;

    public User() {
    }

    public User(String name, String phone, String password, String shopName) {
        Name = name;
        Phone = phone;
        Password = password;
        ShopName = shopName;
    }

    public String getNamee() {
        return Name;
    }

    public void setNamee(String name) {
        Name = name;
    }

    public String getPasswordd() {
        return Password;
    }

    public void setPasswordd(String password) {
        Password = password;
    }

    public String getPhonee() {
        return Phone;
    }

    public void setPhonee(String phone) {
        Phone = phone;
    }

    public String getShopNamee() {
        return ShopName;
    }

    public void setShopNamee(String shopName) {
        ShopName = shopName;
    }
}
