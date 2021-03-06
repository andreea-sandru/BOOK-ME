package com.example.bookme.ObjectModels;

import java.io.Serializable;
import java.util.HashMap;

public class BookObject implements Serializable {

    private String userId; // id-ul userului care a rezervat cartea
    private String bookId; // id-ul cartii din Firebase Database
    private String bookName;
    private String bookYear;
    private String bookCategory;
    private String bookAuthor;
    private String imageUri;
    private boolean available;
    private String reservedDate;
    private String estimatedTime;
    private String reservedUsername;
    private String bookCategoryAndStatus; // I need this for filtering

    public HashMap<String, String> notifyUserIds = new HashMap<String, String>();
    // merge pus la cheie id-ul userului de notificat si la valoare orice

    // cand modificam starea "available", trebuie sa modificam si reservedUserId
    private String reservedUserId; //id-ul userului care rezerva cartea

    public BookObject() {

    }

    public BookObject(String bookId, String userId, String imageUri, String bookName, String bookAuthor, String bookCategory, String bookYear) {
        this.bookId = bookId;
        this.userId = userId;
        this.bookAuthor = bookAuthor;
        this.bookYear = bookYear;
        this.bookName = bookName;
        this.bookCategory = bookCategory;
        this.imageUri = imageUri;
        this.available = true;
        this.reservedUserId = "";
        this.reservedDate = "";
        this.estimatedTime = "";
        this.reservedUsername = "";
        this.bookCategoryAndStatus = this.bookCategory + " true";
    }

    public String getStatus() {
        if (isAvailable() == true) {
            return "available";
        }
        return "not available";
    }

    public String getReservedUserId() {
        return reservedUserId;
    }

    public void setReservedUserId(String reservedUserId) {
        this.reservedUserId = reservedUserId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookYear() {
        return bookYear;
    }

    public void setBookYear(String bookYear) {
        this.bookYear = bookYear;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedBookTime) {
        this.estimatedTime = estimatedBookTime;
    }

    public String getReservedUsername() {
        return reservedUsername;
    }

    public void setReservedUsername(String reservedUsername) {
        this.reservedUsername = reservedUsername;
    }

    public HashMap<String, String> getNotifyUserIds() {
        return notifyUserIds;
    }

    public void setNotifyUserIds(HashMap<String, String> notifyUserIds) {
        this.notifyUserIds = notifyUserIds;
    }

    public String getBookCategoryAndStatus() {
        return bookCategoryAndStatus;
    }

    public void setBookCategoryAndStatus(String bookCategoryAndStatus) {
        this.bookCategoryAndStatus = bookCategoryAndStatus;
    }


}
