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

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getTheme() {
        return theme;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getEditeur() {
        return editeur;
    }

    public String getLieu() {
        return lieu;
    }

    public String getAnnee() {
        return annee;
    }
}