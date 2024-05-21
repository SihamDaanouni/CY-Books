package com.example.cybook;

public class Borrow {
    private String ISBN;
    private String mail;
    private String name;
    private String firstname;
    private String title;
    private String timeBorrowStart;
    private String timeBorrowEnd;
    private String isReturn;
    private int nbDelays;

    public Borrow(String ISBN, String mail, String name, String firstname, String title, String timeBorrowStart, String timeBorrowEnd, String isReturn, int nbDelays) {
        this.ISBN = ISBN;
        this.mail = mail;
        this.name = name;
        this.firstname = firstname;
        this.title = title;
        this.timeBorrowStart = timeBorrowStart;
        this.timeBorrowEnd = timeBorrowEnd;
        this.isReturn = isReturn;
        this.nbDelays = nbDelays;
    }

    // Getters
    public String getISBN() { return ISBN; }
    public String getMail() { return mail; }
    public String getName() { return name; }
    public String getFirstname() { return firstname; }
    public String getTitle() { return title; }
    public String getTimeBorrowStart() { return timeBorrowStart; }
    public String getTimeBorrowEnd() { return timeBorrowEnd; }
    public String getIsReturn() { return isReturn; }
    public int getNbDelays() { return nbDelays; }
}
