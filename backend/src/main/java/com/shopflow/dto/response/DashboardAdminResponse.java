package com.shopflow.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class DashboardAdminResponse {

    private BigDecimal chiffreAffairesGlobal; private Long totalCommandes;
    private Long totalUtilisateurs; private Long totalProduits;
    private List<ProductResponse> topProduits; private List<SellerSummaryResponse> topVendeurs;
    private List<OrderResponse> commandesRecentes;

    public DashboardAdminResponse() {}

    public DashboardAdminResponse(BigDecimal chiffreAffairesGlobal, Long totalCommandes,
                                   Long totalUtilisateurs, Long totalProduits,
                                   List<ProductResponse> topProduits,
                                   List<SellerSummaryResponse> topVendeurs,
                                   List<OrderResponse> commandesRecentes) {
        this.chiffreAffairesGlobal = chiffreAffairesGlobal; this.totalCommandes = totalCommandes;
        this.totalUtilisateurs = totalUtilisateurs; this.totalProduits = totalProduits;
        this.topProduits = topProduits; this.topVendeurs = topVendeurs;
        this.commandesRecentes = commandesRecentes;
    }

    public BigDecimal getChiffreAffairesGlobal() { return chiffreAffairesGlobal; }
    public void setChiffreAffairesGlobal(BigDecimal chiffreAffairesGlobal) { this.chiffreAffairesGlobal = chiffreAffairesGlobal; }
    public Long getTotalCommandes() { return totalCommandes; }
    public void setTotalCommandes(Long totalCommandes) { this.totalCommandes = totalCommandes; }
    public Long getTotalUtilisateurs() { return totalUtilisateurs; }
    public void setTotalUtilisateurs(Long totalUtilisateurs) { this.totalUtilisateurs = totalUtilisateurs; }
    public Long getTotalProduits() { return totalProduits; }
    public void setTotalProduits(Long totalProduits) { this.totalProduits = totalProduits; }
    public List<ProductResponse> getTopProduits() { return topProduits; }
    public void setTopProduits(List<ProductResponse> topProduits) { this.topProduits = topProduits; }
    public List<SellerSummaryResponse> getTopVendeurs() { return topVendeurs; }
    public void setTopVendeurs(List<SellerSummaryResponse> topVendeurs) { this.topVendeurs = topVendeurs; }
    public List<OrderResponse> getCommandesRecentes() { return commandesRecentes; }
    public void setCommandesRecentes(List<OrderResponse> commandesRecentes) { this.commandesRecentes = commandesRecentes; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private BigDecimal chiffreAffairesGlobal; private Long totalCommandes;
        private Long totalUtilisateurs; private Long totalProduits;
        private List<ProductResponse> topProduits; private List<SellerSummaryResponse> topVendeurs;
        private List<OrderResponse> commandesRecentes;

        public Builder chiffreAffairesGlobal(BigDecimal c) { this.chiffreAffairesGlobal = c; return this; }
        public Builder totalCommandes(Long totalCommandes) { this.totalCommandes = totalCommandes; return this; }
        public Builder totalUtilisateurs(Long totalUtilisateurs) { this.totalUtilisateurs = totalUtilisateurs; return this; }
        public Builder totalProduits(Long totalProduits) { this.totalProduits = totalProduits; return this; }
        public Builder topProduits(List<ProductResponse> topProduits) { this.topProduits = topProduits; return this; }
        public Builder topVendeurs(List<SellerSummaryResponse> topVendeurs) { this.topVendeurs = topVendeurs; return this; }
        public Builder commandesRecentes(List<OrderResponse> commandesRecentes) { this.commandesRecentes = commandesRecentes; return this; }

        public DashboardAdminResponse build() {
            return new DashboardAdminResponse(chiffreAffairesGlobal, totalCommandes, totalUtilisateurs,
                    totalProduits, topProduits, topVendeurs, commandesRecentes);
        }
    }
}
