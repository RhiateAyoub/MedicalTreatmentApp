package model;

import javafx.beans.property.*;


public class Patient {
    private final SimpleStringProperty nom;
    private final SimpleStringProperty prenom;
    private final SimpleStringProperty dateNaissance;
    private final SimpleStringProperty sexe;

    public Patient(String nom, String prenom, String dateNaissance, String sexe) {
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.dateNaissance = new SimpleStringProperty(dateNaissance);
        this.sexe = new SimpleStringProperty(sexe);
    }

    public String getNom() {
        return nom.get();
    }

    public String getPrenom() {
        return prenom.get();
    }

    public String getDateNaissance() {
        return dateNaissance.get();
    }

    public String getSexe() {
        return sexe.get();
    }
}
