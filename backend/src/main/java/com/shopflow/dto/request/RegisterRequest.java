package com.shopflow.dto.request;

import com.shopflow.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @Email(message = "Email invalide")
    @NotBlank(message = "Email obligatoire")
    private String email;

    @NotBlank(message = "Mot de passe obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
             message = "Le mot de passe doit contenir au moins une majuscule, une minuscule et un chiffre")
    private String motDePasse;

    @NotBlank(message = "Prénom obligatoire")
    private String prenom;

    @NotBlank(message = "Nom obligatoire")
    private String nom;

    private Role role = Role.CUSTOMER;

    private String nomBoutique;
    private String descriptionBoutique;
    private String logo;

    public RegisterRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getNomBoutique() { return nomBoutique; }
    public void setNomBoutique(String nomBoutique) { this.nomBoutique = nomBoutique; }
    public String getDescriptionBoutique() { return descriptionBoutique; }
    public void setDescriptionBoutique(String descriptionBoutique) { this.descriptionBoutique = descriptionBoutique; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
}
