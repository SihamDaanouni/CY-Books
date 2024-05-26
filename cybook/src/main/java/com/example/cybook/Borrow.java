package com.example.cybook;

public class Borrow {  // declaration of the object used to build a loan

    private String ISBN; // recovery of the International Standard Book Number to borrow
    private String mail; // recovery of mail to borrow
    private String name; // recovery of name to borrow
    private String firstname; // recovery of firstname to borrow
    private String title; // recovery of title to borrow
    private long timeBorrowStart; //recovery of the borrowing date
    private long timeBorrowEnd; //defined the date of delivery
    private boolean isReturn; // define whether the book is returned or not
    private String formattedBorrowStart; // converted to readable format
    private String formattedBorrowEnd;   // converted to readable format

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

    /**
     * Constructor for Borrow
     *
     * @param ISBN
     * @param mail
     * @param name
     * @param firstname
     * @param title
     * @param timeBorrowStart
     * @param timeBorrowEnd
     * @param isReturn
     */
    public Borrow(String ISBN, String mail, String name, String firstname, String title, long timeBorrowStart, long timeBorrowEnd, boolean isReturn) {
        this.ISBN = ISBN;
        this.mail = mail;
        this.name = name;
        this.firstname = firstname;
        this.title = title;                           // constructor of the borrowing object
        this.timeBorrowStart = timeBorrowStart;
        this.timeBorrowEnd = timeBorrowEnd;
        this.isReturn = isReturn;
    }

    public boolean getIsReturn() {
        return isReturn;
    }
}