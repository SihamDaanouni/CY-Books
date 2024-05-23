package com.example.cybook;

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

    // Existing getters and setters

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimeBorrowStart() {
        return timeBorrowStart;
    }

    public void setTimeBorrowStart(long timeBorrowStart) {
        this.timeBorrowStart = timeBorrowStart;
    }

    public long getTimeBorrowEnd() {
        return timeBorrowEnd;
    }

    public void setTimeBorrowEnd(long timeBorrowEnd) {
        this.timeBorrowEnd = timeBorrowEnd;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public String getFormattedBorrowStart() {
        return formattedBorrowStart;
    }

    public void setFormattedBorrowStart(String formattedBorrowStart) {
        this.formattedBorrowStart = formattedBorrowStart;
    }

    public String getFormattedBorrowEnd() {
        return formattedBorrowEnd;
    }

    public void setFormattedBorrowEnd(String formattedBorrowEnd) {
        this.formattedBorrowEnd = formattedBorrowEnd;
    }

    // Constructors

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

    public boolean getIsReturn() {
        return isReturn;
    }
}