package com.shopflow.dto.response;

public class AddressResponse {

    private Long id; private String rue; private String ville;
    private String codePostal; private String pays; private boolean principal;

    public AddressResponse() {}

    public AddressResponse(Long id, String rue, String ville, String codePostal, String pays, boolean principal) {
        this.id = id; this.rue = rue; this.ville = ville;
        this.codePostal = codePostal; this.pays = pays; this.principal = principal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String rue; private String ville;
        private String codePostal; private String pays; private boolean principal;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder rue(String rue) { this.rue = rue; return this; }
        public Builder ville(String ville) { this.ville = ville; return this; }
        public Builder codePostal(String codePostal) { this.codePostal = codePostal; return this; }
        public Builder pays(String pays) { this.pays = pays; return this; }
        public Builder principal(boolean principal) { this.principal = principal; return this; }

        public AddressResponse build() { return new AddressResponse(id, rue, ville, codePostal, pays, principal); }
    }
}
