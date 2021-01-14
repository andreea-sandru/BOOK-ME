package com.example.bookme.ObjectModels;

public class BookObject {

    private String userId; // id-ul userului care a rezervat cartea
    private String bookId; // id-ul cartii din Firebase Database
    private String bookName;
    private String bookYear;
    private String bookCategory;
    private String bookAuthor;
    private String imageUri;
    private boolean available;

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
}
