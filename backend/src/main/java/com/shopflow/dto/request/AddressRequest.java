package com.shopflow.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AddressRequest {

    @NotBlank
    private String rue;

    @NotBlank
    private String ville;

    @NotBlank
    private String codePostal;

    @NotBlank
    private String pays;

    private boolean principal = false;

    public AddressRequest() {}

    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public boolean isPrincipal() { return principal; }
    public void setPrincipal(boolean principal) { this.principal = principal; }
}
