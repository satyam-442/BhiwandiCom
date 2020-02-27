package com.example.bhiwandicom.Model;

public class MainModel
{
    Integer horiCategoryRecyclerViewImage;
    String horiCategoryRecyclerViewText;

    public MainModel(Integer horiCategoryRecyclerViewImage,String horiCategoryRecyclerViewText)
    {
        this.horiCategoryRecyclerViewImage = horiCategoryRecyclerViewImage;
        this.horiCategoryRecyclerViewText = horiCategoryRecyclerViewText;
    }

    public Integer getHoriCategoryRecyclerViewImagee() {
        return horiCategoryRecyclerViewImage;
    }

    public void setHoriCategoryRecyclerViewImagee(Integer horiCategoryRecyclerViewImage) {
        this.horiCategoryRecyclerViewImage = horiCategoryRecyclerViewImage;
    }

    public String getHoriCategoryRecyclerViewTextt() {
        return horiCategoryRecyclerViewText;
    }

    public void setHoriCategoryRecyclerViewTextt(String horiCategoryRecyclerViewText) {
        this.horiCategoryRecyclerViewText = horiCategoryRecyclerViewText;
    }
}
