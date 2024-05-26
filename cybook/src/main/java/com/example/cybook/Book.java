package com.example.cybook;

/**
 * Book
 * class book
 */
public class Book {  // declaration object book with all the information concerning it
    private int id;     //
    private String titre;   // book title
    private String auteur; //book author
    private String theme; //about the book
    private String isbn; //L'International Standard Book Number
    private String editeur; // book publisher
    private String lieu; //publication location
    private String annee; // publication year

    /**
     * Constructor for Book
     *
     * @param id comptor of all book
     * @param titre titre of  book
     * @param auteur auteur of book
     * @param theme theme of book
     * @param isbn international standard book number of book
     * @param editeur editeur of book
     * @param lieu place of publisher
     * @param annee of publisher
     */
    public Book(int id, String titre, String auteur, String theme, String isbn, String editeur, String lieu, String annee) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;   // book object builder
        this.theme = theme;
        this.isbn = isbn;
        this.editeur = editeur;
        this.lieu = lieu;
        this.annee = annee;
    }

    /**
     *getter Id
     * @return id
     */

    public int getId() {
        return id;
    }
    /**
     *getter Title
     * @return titre
     */

    public String getTitre() {
        return titre;
    }
    /**
     * getter Author
     * @return auteur
     */

    public String getAuteur() {
        return auteur;
    }
    /**
     * getter Theme
     * @return theme
     */

    public String getTheme() {
        return theme;
    }
    /**
     * get ISBN
     * @return isbn
     */

    public String getIsbn() {
        return isbn;
    }
    /**
     *  get Editor
     * @return editeur
     */

    public String getEditeur() {
        return editeur;
    }
    /**
     * get Place
     * @return place
     */

    public String getLieu() {
        return lieu;
    }
    /**
     * get Year
     * @return year
     */

    public String getAnnee() {
        return annee;
    }
}