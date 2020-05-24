package com.example.bhiwandicom.Model;

public class Products
{
    public String Category, Date, Description, Pname, Price, Time, image, pid, ShopName;

    public Products() {
    }

    public Products(String category, String date, String description, String pname, String price, String time, String image, String pid, String shopName) {
        Category = category;
        Date = date;
        Description = description;
        Pname = pname;
        Price = price;
        Time = time;
        this.image = image;
        this.pid = pid;
        ShopName = shopName;
    }

    public String getCategoryy() {
        return Category;
    }

    public void setCategoryy(String category) {
        Category = category;
    }

    public String getDatee() {
        return Date;
    }

    public void setDatee(String date) {
        Date = date;
    }

    public String getDescriptionn() {
        return Description;
    }

    public void setDescriptionn(String description) {
        Description = description;
    }

    public String getPnamee() {
        return Pname;
    }

    public void setPnamee(String pname) {
        Pname = pname;
    }

    public String getPricee() {
        return Price;
    }

    public void setPricee(String price) {
        Price = price;
    }

    public String getTimee() {
        return Time;
    }

    public void setTimee(String time) {
        Time = time;
    }

    public String getImagee() {
        return image;
    }

    public void setImagee(String image) {
        this.image = image;
    }

    public String getPidd() {
        return pid;
    }

    public void setPidd(String pid) {
        this.pid = pid;
    }

    public String getShopNamee() {
        return ShopName;
    }

    public void setShopNamee(String shopName) {
        ShopName = shopName;
    }
}
