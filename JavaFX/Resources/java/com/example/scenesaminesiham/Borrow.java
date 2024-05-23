package com.example.scenesaminesiham;

public class Borrow {
    private String ISBN;
    private String mail;
    private String name;
    private String firstname;
    private String title;
    private long timeBorrowStart;
    private long timeBorrowEnd;
    private boolean isReturn;
    private String formattedBorrowStart;
    private String formattedBorrowEnd;

    public Borrow(String ISBN, String mail, String name, String firstname, String title, long timeBorrowStart, long timeBorrowEnd, boolean isReturn) {
        this.ISBN = ISBN;
        this.mail = mail;
        this.name = name;
        this.firstname = firstname;
        this.title = title;
        this.timeBorrowStart = timeBorrowStart;
        this.timeBorrowEnd = timeBorrowEnd;
        this.isReturn = isReturn;
    }
    public void setFormattedBorrowStart(String formattedBorrowStart) {
        this.formattedBorrowStart = formattedBorrowStart;
    }
    public void setFormattedBorrowEnd(String formattedBorrowEnd) {
        this.formattedBorrowEnd = formattedBorrowEnd;
    }

    public String getISBN() { return ISBN; }
    public String getMail() { return mail; }
    public String getName() { return name; }
    public String getFirstname() { return firstname; }
    public String getTitle() { return title; }
    public long getTimeBorrowStart() { return timeBorrowStart; }
    public long getTimeBorrowEnd() { return timeBorrowEnd; }
    public boolean getIsReturn() {
        return isReturn;
    }

}