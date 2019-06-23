package com.example.booklistingapp;

public class Book {

    private String mImageUrl;
    private String mAuthor;
    private String mTitle;
    private String mLang;
    private String mCategory;
    private String mUrl;

    public Book(String imageUrl, String author, String title, String lang, String category, String url) {
        mImageUrl = imageUrl;
        mAuthor = author;
        mTitle = title;
        mLang = lang;
        mCategory = category;
        mUrl = url;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLang() {
        return mLang;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getUrl() {
        return mUrl;
    }
}
