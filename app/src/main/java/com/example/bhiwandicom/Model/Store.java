package com.example.bhiwandicom.Model;

public class Store {
    public String OwnerName, OwnerPhone, Password, ShopAddress, ShopName, fromTime, toTime, image;

    public Store() {
    }

    public Store(String ownerName, String ownerPhone, String password, String shopAddress, String shopName, String fromTime, String toTime, String image) {
        OwnerName = ownerName;
        OwnerPhone = ownerPhone;
        Password = password;
        ShopAddress = shopAddress;
        ShopName = shopName;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.image = image;
    }

    public String getOwnerNamee() {
        return OwnerName;
    }

    public void setOwnerNamee(String ownerName) {
        OwnerName = ownerName;
    }

    public String getOwnerPhonee() {
        return OwnerPhone;
    }

    public void setOwnerPhonee(String ownerPhone) {
        OwnerPhone = ownerPhone;
    }

    public String getPasswordd() {
        return Password;
    }

    public void setPasswordd(String password) {
        Password = password;
    }

    public String getShopAddresss() {
        return ShopAddress;
    }

    public void setShopAddresss(String shopAddress) {
        ShopAddress = shopAddress;
    }

    public String getShopNamee() {
        return ShopName;
    }

    public void setShopNamee(String shopName) {
        ShopName = shopName;
    }

    public String getFromTimee() {
        return fromTime;
    }

    public void setFromTimee(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTimee() {
        return toTime;
    }

    public void setToTimee(String toTime) {
        this.toTime = toTime;
    }

    public String getImagee() {
        return image;
    }

    public void setImagee(String image) {
        this.image = image;
    }
}
