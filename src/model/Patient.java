/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Classe modèle représentant un patient dans l'application MediConnect
 * Stocke les informations personnelles et l'état de sélection d'un patient
 * 
 * @author pc
 */

public class Patient {
    
    // ============================================================
    // ================ ATTRIBUTS ET PROPRIÉTÉS ===================
    // ============================================================
    
    // --- Informations personnelles du patient
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String sexe;
    
    // --- Propriétés observables
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    
    // ============================================================
    // ====================== CONSTRUCTEUR ========================
    // ============================================================
    
    public Patient(String nom, String prenom, String dateNaissance, String sexe) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
    }
    
    // ============================================================
    // ================ ACCESSEURS ET MUTATEURS ===================
    // ============================================================
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }
    
    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }
        
    // ============================================================
    // ============= GESTION DE L'ÉTAT DE SÉLECTION ===============
    // ============================================================
    
    // Récupère l'état de sélection du patient
    public BooleanProperty selectedProperty() {
        return selected;
    }
    // Vérifie si le patient est sélectionné
    public boolean isSelected() {
        return selected.get();
    }
    // Modifie l'état de sélection du patient
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}