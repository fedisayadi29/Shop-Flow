package com.shopflow.dto.request;

import jakarta.validation.constraints.NotNull;

public class OrderRequest {

    @NotNull(message = "ID adresse de livraison obligatoire")
    private Long addressId;

    public OrderRequest() {}

    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }
}
