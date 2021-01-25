package com.example.bookme.ObjectModels;

public class HistoryObject {

    private String bookId;
    private String userFullName;
    private String startDate;
    private String endDate;

    public HistoryObject() {

    }

    public HistoryObject(String bookId, String userFullName, String startDate, String endDate) {
        this.bookId = bookId;
        this.userFullName = userFullName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
