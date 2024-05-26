package com.example.cybook;

/**
 * class Borrow
 */
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

    /**
     * getISBN
     *
     * @return ISBN
     */

    public String getISBN() {
        return ISBN;
    }

    /**
     * setISBN
     * @param ISBN international serial book number
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    /**
     * getMail
     *
     * @return mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * setMail
     *
     * @param mail mail client
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * getName
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * setName
     * @param name client name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getFirstname
     * @return firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     *setFirstname
     * fix client firstname
     * @param firstname firstname of  borrow client
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    /**
     * getTitle
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * setTitle
     * fix title of book
     *
     * @param title title of book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getTimeBorrowStart
     * @return timeBorrowStart
     */
    public long getTimeBorrowStart() {
        return timeBorrowStart;
    }

    /**
     * setTimeBorrowStart
     * @param timeBorrowStart date start borrow book
     */

    public void setTimeBorrowStart(long timeBorrowStart) {
        this.timeBorrowStart = timeBorrowStart;
    }


    /**
     * getTimeBorrowEnd
     * @return timeBorrowEnd
     */

    public long getTimeBorrowEnd() {
        return timeBorrowEnd;
    }

    /**
     * setTimeBorrowEnd
     * set return borrow book
     * @param timeBorrowEnd  date return borrow book
     */

    public void setTimeBorrowEnd(long timeBorrowEnd) {
        this.timeBorrowEnd = timeBorrowEnd;
    }


    /**
     * isReturn
     * @return isReturn
     */

    public boolean isReturn() {
        return isReturn;
    }

    /**
     * setReturn
     * booleenne statement return
     * @param aReturn statement of return
     */
    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    /**
     * getFormattedBorrowStart
     * -
     * @return formattedBorrowStart
     */
    public String getFormattedBorrowStart() {
        return formattedBorrowStart;
    }

    /**
     *setFormattedBorrowStart
     * borrow starting
     * @param formattedBorrowStart  date borrow starting
     */
    public void setFormattedBorrowStart(String formattedBorrowStart) {
        this.formattedBorrowStart = formattedBorrowStart;
    }

    /**
     * getFormattedBorrowEnd
     * @return formattedBorrowEnd
     */
    public String getFormattedBorrowEnd() {
        return formattedBorrowEnd;
    }

    /**
     *setFormattedBorrowEnd
     * date fix return borrow
     * @param formattedBorrowEnd date return borrow
     */
    public void setFormattedBorrowEnd(String formattedBorrowEnd) {
        this.formattedBorrowEnd = formattedBorrowEnd;
    }

    /**
     * Constructor for Borrow
     *
     * @param ISBN international standard borrow book
     * @param mail mail of client
     * @param name name of borrow book
     * @param firstname firstname of client
     * @param title title of borrow book
     * @param timeBorrowStart date of borrow book
     * @param timeBorrowEnd date of return book
     * @param isReturn statement of borrow
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

    /**
     * getIsReturn
     * @return isReturn
     */

    public boolean getIsReturn() {
        return isReturn;
    }
}