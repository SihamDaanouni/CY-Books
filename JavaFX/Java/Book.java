package com.example.cybook;

public class Book {
    private int id;
    private String titre;
    private String auteur;
    private String theme;
    private String isbn;
    private String editeur;
    private String lieu;
    private String annee;

    /**
     * Constructor for Book
     *
     * @param id
     * @param titre
     * @param auteur
     * @param theme
     * @param isbn
     * @param editeur
     * @param lieu
     * @param annee
     */
    public Book(int id, String titre, String auteur, String theme, String isbn, String editeur, String lieu, String annee) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.theme = theme;
        this.isbn = isbn;
        this.editeur = editeur;
        this.lieu = lieu;
        this.annee = annee;
    }
    // getter Id
    public int getId() {
        return id;
    }
    // getter Title
    public String getTitre() {
        return titre;
    }
    // getter Author
    public String getAuteur() {
        return auteur;
    }
    // getter Theme
    public String getTheme() {
        return theme;
    }
    // get ISBN
    public String getIsbn() {
        return isbn;
    }
    // get Editor
    public String getEditeur() {
        return editeur;
    }
    // get Place
    public String getLieu() {
        return lieu;
    }
    // get Year
    public String getAnnee() {
        return annee;
    }
}