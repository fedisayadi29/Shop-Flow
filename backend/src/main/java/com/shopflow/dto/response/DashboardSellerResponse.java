package com.shopflow.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class DashboardSellerResponse {

    private BigDecimal revenus; private Long commandesEnAttente; private Long totalProduits;
    private Long alertesStockFaible; private List<ProductResponse> produitsStockFaible;
    private List<OrderResponse> commandesRecentes;

    public DashboardSellerResponse() {}

    public DashboardSellerResponse(BigDecimal revenus, Long commandesEnAttente, Long totalProduits,
                                    Long alertesStockFaible, List<ProductResponse> produitsStockFaible,
                                    List<OrderResponse> commandesRecentes) {
        this.revenus = revenus; this.commandesEnAttente = commandesEnAttente;
        this.totalProduits = totalProduits; this.alertesStockFaible = alertesStockFaible;
        this.produitsStockFaible = produitsStockFaible; this.commandesRecentes = commandesRecentes;
    }

    public BigDecimal getRevenus() { return revenus; }
    public void setRevenus(BigDecimal revenus) { this.revenus = revenus; }
    public Long getCommandesEnAttente() { return commandesEnAttente; }
    public void setCommandesEnAttente(Long commandesEnAttente) { this.commandesEnAttente = commandesEnAttente; }
    public Long getTotalProduits() { return totalProduits; }
    public void setTotalProduits(Long totalProduits) { this.totalProduits = totalProduits; }
    public Long getAlertesStockFaible() { return alertesStockFaible; }
    public void setAlertesStockFaible(Long alertesStockFaible) { this.alertesStockFaible = alertesStockFaible; }
    public List<ProductResponse> getProduitsStockFaible() { return produitsStockFaible; }
    public void setProduitsStockFaible(List<ProductResponse> produitsStockFaible) { this.produitsStockFaible = produitsStockFaible; }
    public List<OrderResponse> getCommandesRecentes() { return commandesRecentes; }
    public void setCommandesRecentes(List<OrderResponse> commandesRecentes) { this.commandesRecentes = commandesRecentes; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private BigDecimal revenus; private Long commandesEnAttente; private Long totalProduits;
        private Long alertesStockFaible; private List<ProductResponse> produitsStockFaible;
        private List<OrderResponse> commandesRecentes;

        public Builder revenus(BigDecimal revenus) { this.revenus = revenus; return this; }
        public Builder commandesEnAttente(Long commandesEnAttente) { this.commandesEnAttente = commandesEnAttente; return this; }
        public Builder totalProduits(Long totalProduits) { this.totalProduits = totalProduits; return this; }
        public Builder alertesStockFaible(Long alertesStockFaible) { this.alertesStockFaible = alertesStockFaible; return this; }
        public Builder produitsStockFaible(List<ProductResponse> produitsStockFaible) { this.produitsStockFaible = produitsStockFaible; return this; }
        public Builder commandesRecentes(List<OrderResponse> commandesRecentes) { this.commandesRecentes = commandesRecentes; return this; }

        public DashboardSellerResponse build() {
            return new DashboardSellerResponse(revenus, commandesEnAttente, totalProduits,
                    alertesStockFaible, produitsStockFaible, commandesRecentes);
        }
    }
}
