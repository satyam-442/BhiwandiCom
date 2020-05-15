package com.example.bhiwandicom.Model;

public class Store {
    public String OwnerName, OwnerPhone, Password, ShopAddress, ShopName;

    public Store() {
    }

    public Store(String ownerName, String ownerPhone, String password, String shopAddress, String shopName) {
        OwnerName = ownerName;
        OwnerPhone = ownerPhone;
        Password = password;
        ShopAddress = shopAddress;
        ShopName = shopName;
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
}
